package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.*;
import org.example.cmc_backend.Models.DTO.AiContextResult;
import org.example.cmc_backend.Repository.*;
import org.example.cmc_backend.Service.AiContextService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AiContextServiceImplement implements AiContextService {
    private static final int MAX_PRODUCTS = 8;
    private static final int MAX_CATEGORIES = 8;
    private static final int MAX_SCHEDULE = 8;
    private static final int MAX_BRANCH = 5;
    private static final int MAX_VOUCHER = 5;
    private static final int MAX_BILL = 10;
    private static final List<String> AI_ENTITIES = List.of(
            "Movies",
            "Category",
            "Branchs",
            "Schedules",
            "Vouchers"
    );
    private static final Set<String> STOP_WORDS = Set.of(
            "toi", "minh", "ban", "cho", "hoi", "ve", "la", "co", "khong", "nao", "nhung",
            "cac", "mot", "cua", "trong", "ngoai", "can", "muon", "mua", "san", "pham",
            "hay", "biet", "tat", "ca", "thong", "tin", "toan", "bo", "chi", "tiet"
    );

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final BranchRepository branchRepository;
    private final ScheduleRepository scheduleRepository;
    private final VoucherRepository voucherRepository;
    private final BillRepository billRepository;

    public AiContextServiceImplement(
            MovieRepository movieRepository,
            CategoryRepository categoryRepository,
            BranchRepository branchRepository,
            ScheduleRepository scheduleRepository,
            VoucherRepository voucherRepository,
            BillRepository billRepository
    ) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
        this.branchRepository = branchRepository;
        this.scheduleRepository = scheduleRepository;
        this.voucherRepository = voucherRepository;
        this.billRepository = billRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public AiContextResult buildContext(UserEntity user, String question) {
        String normalizedQuestion = normalize(question);
        String intent = detectIntent(normalizedQuestion);
        if ("OUT_OF_SCOPE".equals(intent)) {
            return new AiContextResult(intent, AI_ENTITIES, 0, "", List.of());
        }

        List<String> terms = searchTerms(normalizedQuestion);
        List<MovieEntity> movieEntities = findMovies(intent, terms);
        List<CategoryEntity> categoryEntities = findCategories(intent, terms);
        List<VoucherEntity> voucherEntities = findVoucher(intent, terms);
        List<BranchEntity> branchEntities = finBranch(intent, terms);
        List<ScheduleEntity> scheduleEntities = finSche(intent, terms);
        List<BillEntity> billEntities = findBill(user, intent, terms);

        StringBuilder context = new StringBuilder();
        List<String> summary = new ArrayList<>();
        appendCategoryContext(context, summary, categoryEntities);
        appendMovieContext(context, summary, movieEntities);
        appendVoucherContext(context, summary, voucherEntities);
        appendBranchContext(context, summary, branchEntities);
        appendScheduleContext(context, summary, scheduleEntities);
        appendBillContext(context, summary, billEntities);


        int totalRecords = movieEntities.size() + categoryEntities.size() + voucherEntities.size() + branchEntities.size() + scheduleEntities.size() + billEntities.size();
        return new AiContextResult(intent, AI_ENTITIES, totalRecords, context.toString().trim(), summary);
    }

    private String detectIntent(String normalizedQuestion) {
        if (normalizedQuestion == null || normalizedQuestion.isBlank()) {
            return "OUT_OF_SCOPE";
        }
        if (containsAny(normalizedQuestion, "khuyen mai", "giam gia", "voucher", "ma giam", "uu dai", "gia", "bao nhieu", "tien", "re", "dat")) {
            return "VOUCHER";
        }
        if (containsAny(
                normalizedQuestion,
                "movie",
                "phim",
                "phim dang chieu"
        )) {
            return "MOVIE";
        }
        if (containsAny(
                normalizedQuestion,
                "chi nhanh",
                "rap",
                "cum rap"
        )) {
            return "BRANCH";
        }
        if (containsAny(normalizedQuestion, "loai", "danh muc", "category")) {
            return "CATEGORY";
        }
        if (containsAny(
                normalizedQuestion,
                "lich chieu",
                "suat chieu",
                "gio chieu",
                "ngay chieu",
                "lich phim",
                "chieu"
        )) {
            return "SCHEDULE";
        }

        if (containsAny(normalizedQuestion, "bill", "don", "mua", "ve", "don hang", "booking")) {
            return "BILL";
        }

        return "OUT_OF_SCOPE";
    }

    private List<MovieEntity> findMovies(String intent, List<String> terms) {
        boolean movieIntent = List.of("MOVIE", "SCHEDULE", "BRANCH", "CATEGORY", "VOUCHER").contains(intent);
        if (!movieIntent) {
            return List.of();
        }


        return movieRepository.findAll().stream()
                .filter(movie -> terms.isEmpty() || scoreMovie(movie, terms) > 0)
                .limit(MAX_PRODUCTS)
                .sorted(movieComparator(terms))
                .toList();
    }

    public Comparator<MovieEntity> movieComparator(List<String> terms) {
        return Comparator
                .comparing(MovieEntity::isShowing)
                .reversed()
                .thenComparing((MovieEntity movie) -> scoreMovie(movie, terms), Comparator.reverseOrder())
                .thenComparing(MovieEntity::getReleaseDate, Comparator.nullsLast(Comparator.reverseOrder()));
    }

    private int scoreMovie(MovieEntity movie, List<String> terms) {
        CategoryEntity categoryEntity = movie.getCategoryEntity();
        return scoreText(
                terms,
                movie.getIdMovie(),
                movie.getNameMovie(),
                movie.getRated(),
                movie.getLanguage(),
                movie.getDuration(),
                movie.getDirector(),
                movie.getTrailer(),
                movie.getShortDescription(),
                categoryEntity == null ? null : categoryEntity.getNameCategory()
        );
    }

    private List<CategoryEntity> findCategories(String intent, List<String> terms) {
        if (!List.of("CATEGORY", "MOVIE").contains(intent)) {
            return List.of();
        }
        return categoryRepository.findAll().stream()
                .filter(category -> terms.isEmpty() || scoreText(terms, category.getNameCategory()) > 0)
                .limit(MAX_CATEGORIES)
                .toList();
    }

    private List<VoucherEntity> findVoucher(String intent, List<String> terms) {
        if (!"VOUCHER".equals(intent)) {
            return List.of();
        }

        LocalDate dayEnd = LocalDate.now();
        return voucherRepository.findAll().stream()
                .filter(voucherEntity -> voucherEntity.getExpiration() == null || !voucherEntity.getExpiration().isBefore(dayEnd))
                .filter(voucherEntity -> terms.isEmpty() || scoreText(terms, voucherEntity.getCode()) > 0)
                .limit(MAX_VOUCHER)
                .toList();
    }

    private List<BranchEntity> finBranch(String intent, List<String> terms) {
        if (!List.of("BRANCH", "SCHEDULE").contains(intent)) {
            return List.of();
        }

        return branchRepository.findAll().stream()
                .filter(branchEntity -> terms.isEmpty() || scoreBranch(branchEntity, terms) > 0)
                .limit(MAX_BRANCH)
                .toList();
    }

    public int scoreBranch(BranchEntity branch, List<String> terms) {
        return scoreText(terms, branch.getNameBranch(), branch.getAddress(), branch.getPhone());
    }

    public List<ScheduleEntity> finSche(String intent, List<String> terms) {
        if (!List.of("SCHEDULE", "MOVIE", "BRANCH", "VOUCHER").contains(intent)) {
            return List.of();
        }

        LocalTime timeEnd = LocalTime.now();

        return scheduleRepository.findAll().stream()
                .filter(scheduleEntity -> scheduleEntity.getTimeEnd() == null || !scheduleEntity.getTimeEnd().isBefore(timeEnd))
                .filter(scheduleEntity -> terms.isEmpty() || scoreSchedule(scheduleEntity, terms) > 0)
                .limit(MAX_SCHEDULE)
                .toList();
    }

    public int scoreSchedule(ScheduleEntity scheduleEntity, List<String> terms) {
        MovieEntity movieEntity = scheduleEntity.getMovieEntity();
        RoomEntity roomEntity = scheduleEntity.getRoomEntity();
        BranchEntity branchEntity = roomEntity == null ? null : roomEntity.getBranchEntity();

        return scoreText(terms,
                movieEntity.getNameMovie(),
                movieEntity.getDirector(),
                roomEntity.getName(),
                branchEntity.getNameBranch(),
                branchEntity.getAddress(),
                scheduleEntity.getDate().toString(),
                scheduleEntity.getTimeEnd().toString()
        );
    }

    public List<BillEntity> findBill(UserEntity user, String intent, List<String> terms) {
        if (!"BILL".equals(intent)) {
            return List.of();
        }

        if (user == null || user.getIdUser() == null) {
            return List.of();
        }

        return billRepository.findAllByUserEntity(user).stream()
                .sorted(Comparator.comparing((BillEntity billEntity) -> scoreBill(billEntity, terms), Comparator.reverseOrder())
                        .thenComparing((BillEntity::getIdBill), Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(MAX_BILL)
                .toList();
    }

    public int scoreBill(BillEntity bill, List<String> terms) {
        if (terms.isEmpty()){
            return 0;
        }
        BranchEntity branchEntity = bill.getBranchEntity();
        VoucherEntity voucherEntity = bill.getVoucherEntity();
        List<String> result = new ArrayList<>();
        result.add(bill.getIdBill());
        result.add(bill.getTotalAmount().toString());
        result.add(bill.getCreatedAt().toString());
        result.add(branchEntity.getNameBranch());
        result.add(branchEntity.getAddress());
        result.add(voucherEntity.getCode());

        for (TicketEntity ticketEntity : bill.getTicketEntities()) {
            SeatEntity seatEntity = ticketEntity.getSeatEntity();
            SeatTypeEntity seatTypeEntity = seatEntity.getSeatTypeEntity();
            RoomEntity roomEntity = seatEntity.getRoomEntity();
            result.add(ticketEntity.getIdTicket());
            result.add(seatEntity.getName());
            result.add(seatTypeEntity.getType());
            result.add(roomEntity.getName());
        }

        return scoreText(terms, result.toArray(String[]::new));
    }

    private int scoreText(List<String> terms, String... values) {
        int score = 0;
        for (String term : terms) {
            for (String value : values) {
                if (value != null && normalize(value).contains(term)) {
                    score++;
                }
            }
        }
        return score;
    }

    private void appendMovieContext(StringBuilder context, List<String> summary, List<MovieEntity> movieEntities) {
        if (movieEntities.isEmpty()) {
            return;
        }
        context.append("Movies:\n");
        for (MovieEntity movieEntity : movieEntities) {
            CategoryEntity category = movieEntity.getCategoryEntity();
            context.append("- id_movie: ").append(safe(movieEntity == null ? null : movieEntity.getIdMovie())).append("\n");
            context.append("  name_movie: ").append(safe(movieEntity == null ? null : movieEntity.getNameMovie())).append("\n");
            context.append("  director: ").append(shortText(movieEntity == null ? null : movieEntity.getDirector(), 260)).append("\n");
            context.append("  duration: ").append(safe(movieEntity == null ? null : movieEntity.getDuration())).append("\n");
            context.append("  releaseDate: ").append(safe(movieEntity == null ? null : movieEntity.getReleaseDate().toString())).append("\n");
            context.append("  language: ").append(safe(movieEntity == null ? null : movieEntity.getLanguage())).append("\n");
            context.append("  rated: ").append(safe(movieEntity == null ? null : movieEntity.getRated())).append("\n");
            context.append("  isShowing: ").append(movieEntity == null ? null : movieEntity.isShowing()).append("\n");
            context.append("  shortDescription: ").append(movieEntity == null ? null : movieEntity.getShortDescription()).append("\n");
            context.append("  smallImage: ").append(movieEntity == null ? null : movieEntity.getSmallImage()).append("\n");
            context.append("  largeImage: ").append(movieEntity == null ? null : movieEntity.getLargeImage()).append("\n");
            context.append("  trailer: ").append(movieEntity == null ? null : movieEntity.getTrailer()).append("\n");
            context.append("  category_name: ").append(category == null ? null : category.getNameCategory()).append("\n");
            summary.add("Movie" + safe(movieEntity.getNameMovie()) + " " + (movieEntity.isShowing() ? "Đang chiếu" : "Chưa chiếu"));

        }
    }

    private void appendCategoryContext(StringBuilder context, List<String> summary, List<CategoryEntity> categories) {
        if (categories.isEmpty()) {
            return;
        }
        context.append("Categories:\n");
        for (CategoryEntity category : categories) {
            context.append("- id: ").append(safe(category.getIdCategory().toString())).append("\n");
            context.append("  nameCategory: ").append(safe(category.getNameCategory())).append("\n");
            summary.add("Category " + safe(category.getNameCategory()));
        }
    }

    private void appendVoucherContext(StringBuilder context, List<String> summary, List<VoucherEntity> voucherEntities) {
        if (voucherEntities.isEmpty()) {
            return;
        }
        context.append("Vouchers:\n");
        for (VoucherEntity voucherEntity : voucherEntities) {
            context.append("- idVoucher: ").append(safe(voucherEntity.getIdVoucher().toString())).append("\n");
            context.append("  code: ").append(safe(voucherEntity.getCode())).append("\n");
            context.append("  createdAt: ").append(voucherEntity.getCreatedAt()).append("\n");
            context.append("  expiration: ").append(voucherEntity.getExpiration()).append("\n");
            context.append("  discount: ").append(voucherEntity.getDiscount()).append("\n");
            context.append("  quality: ").append(voucherEntity.getQuality()).append("\n");
            summary.add("Voucher " + safe(voucherEntity.getCode()) + " " + voucherEntity.getDiscount());
        }
    }

    private void appendBranchContext(StringBuilder context, List<String> summary, List<BranchEntity> branchEntities) {
        if (branchEntities.isEmpty()) {
            return;
        }

        context.append("Branches:\n");
        for (BranchEntity branchEntity : branchEntities) {
            context.append("- id_branch ").append(safe(branchEntity.getIdBranch().toString())).append("\n");
            context.append("  nameBranch: ").append(safe(branchEntity.getNameBranch())).append("\n");
            context.append("  address: ").append(safe(branchEntity.getAddress())).append("\n");
            context.append("  phone: ").append(safe(branchEntity.getPhone())).append("\n");
            summary.add("Branch " + safe(branchEntity.getNameBranch()) + " " + safe(branchEntity.getAddress()) + " " + safe(branchEntity.getPhone()));
        }
    }

    private void appendScheduleContext(StringBuilder context, List<String> summary, List<ScheduleEntity> scheduleEntities) {
        if (scheduleEntities.isEmpty()) {
            return;
        }
        context.append("Schedules:\n");
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            MovieEntity movieEntity = scheduleEntity.getMovieEntity();
            RoomEntity roomEntity = scheduleEntity.getRoomEntity();
            BranchEntity branchEntity = roomEntity == null ? null : roomEntity.getBranchEntity();
            context.append("- id_schedule: ").append(safe(scheduleEntity.getIdSchedule().toString())).append("\n");
            context.append(" base_price: ").append(safe(scheduleEntity.getBasePrice().toString())).append("\n");
            context.append(" date: ").append(safe(scheduleEntity.getDate().toString())).append("\n");
            context.append(" time_start: ").append(safe(scheduleEntity.getTimeStart().toString())).append("\n");
            context.append(" time_end: ").append(safe(scheduleEntity.getTimeEnd().toString())).append("\n");
            context.append(" name_movie ").append(safe(movieEntity.getNameMovie())).append("\n");
            context.append(" name_room").append(safe(roomEntity.getName())).append("\n");
            context.append(" name_branch ").append(safe(branchEntity.getNameBranch())).append("\n");
            context.append(" address: ").append(safe(branchEntity.getAddress())).append("\n");
            context.append(" phone: ").append(safe(branchEntity.getPhone())).append("\n");
            summary.add("Schedule " + scheduleEntity.getDate() + " " + movieEntity.getNameMovie() + " " + roomEntity.getName() + " " + branchEntity.getNameBranch() + " " + branchEntity.getAddress());
        }
    }

    private void appendBillContext(StringBuilder context, List<String> summary, List<BillEntity> billEntities) {
        if (billEntities.isEmpty()) {
            return;
        }
        context.append("Bills:\n");
        for (BillEntity billEntity : billEntities) {
            BranchEntity branchEntity = billEntity.getBranchEntity();
            VoucherEntity voucherEntity = billEntity.getVoucherEntity();

            context.append("- id_bill: ").append(safe(billEntity.getIdBill().toString())).append("\n");
            context.append(" created_at: ").append(safe(billEntity.getCreatedAt().toString())).append("\n");
            context.append(" total_amount: ").append(safe(billEntity.getTotalAmount().toString())).append("\n");
            context.append(" branch: ").append(safe(branchEntity.getNameBranch())).append("\n");
            context.append(" voucher: ").append(safe(voucherEntity.getCode())).append("\n");

            appendTicketContext(context, billEntity);
            appendFoodContext(context, billEntity);
            appendDrinkContext(context, billEntity);

            summary.add("Bill " + billEntity.getIdBill() + " " + branchEntity.getNameBranch() + " " + branchEntity.getAddress() + " " + voucherEntity.getCode());

        }
    }

    private void appendTicketContext(StringBuilder context, BillEntity billEntity) {
        if (billEntity.getTicketEntities().isEmpty()) {
            return;
        }
        for (TicketEntity ticketEntity : billEntity.getTicketEntities()) {
            SeatEntity seatEntity = ticketEntity.getSeatEntity();
            SeatTypeEntity seatTypeEntity = seatEntity.getSeatTypeEntity();
            RoomEntity roomEntity = seatEntity.getRoomEntity();
            context.append("- id_ticket: ").append(safe(ticketEntity.getIdTicket().toString())).append("\n");
            context.append(" seat_name: ").append(safe(seatEntity.getName())).append("\n");
            context.append(" seat_type: ").append(safe(seatTypeEntity.getType())).append("\n");
            context.append(" room_name: ").append(safe(roomEntity.getName())).append("\n");
        }
    }

    private void appendDrinkContext(StringBuilder context, BillEntity billEntity) {
        if (billEntity.getBillDrinkDetailEntities().isEmpty()) {
            return;
        }
        for (BillDrinkDetailEntity detailEntity : billEntity.getBillDrinkDetailEntities()) {
            SizeDrinkEntity sizeDrinkEntity = detailEntity.getSizeDrinkEntity();
            DrinkEntity drinkEntity = sizeDrinkEntity.getDrinkEntity();
            context.append(" name_drink: ").append(safe(drinkEntity.getName())).append("\n");
            context.append(" size: ").append(safe(sizeDrinkEntity.getSizeEntity().getSize())).append("\n");
            context.append(" quality: ").append(detailEntity.getQuality()).append("\n");
            context.append(" price: ").append(sizeDrinkEntity.getPrice()).append("\n");
            context.append(" total: ").append(detailEntity.getTotal()).append("\n");
        }
    }

    private void appendFoodContext(StringBuilder context, BillEntity billEntity) {
        if (billEntity.getBillFoodDetailEntities().isEmpty()) {
            return;
        }
        for (BillFoodDetailEntity detailEntity : billEntity.getBillFoodDetailEntities()) {
            SizeFoodEntity sizeFoodEntity = detailEntity.getSizeFoodEntity();
            FoodEntity foodEntity = sizeFoodEntity.getFoodEntity();
            context.append(" name_food: ").append(safe(foodEntity.getName())).append("\n");
            context.append(" size: ").append(safe(sizeFoodEntity.getSizeEntity().getSize())).append("\n");
            context.append(" quality: ").append(detailEntity.getQuality()).append("\n");
            context.append(" price: ").append(sizeFoodEntity.getPrice()).append("\n");
            context.append(" total: ").append(detailEntity.getTotal()).append("\n");
        }
    }

    private List<String> searchTerms(String normalizedQuestion) {
        if (normalizedQuestion == null || normalizedQuestion.isBlank()) {
            return List.of();
        }
        Set<String> terms = new LinkedHashSet<>();
        for (String term : normalizedQuestion.split("[^a-z0-9]+")) {
            if (term.length() >= 2 && !STOP_WORDS.contains(term)) {
                terms.add(term);
            }
        }
        return new ArrayList<>(terms);
    }

    private List<String> nonGenericPriceTerms(List<String> terms) {
        Set<String> genericPriceTerms = Set.of(
                "gia", "bao", "nhieu", "tien", "re", "dat", "nhat",
                "khuyen", "mai", "giam", "uu", "dai", "ma", "coupon", "voucher", "code"
        );
        return terms.stream()
                .filter(term -> !genericPriceTerms.contains(term))
                .toList();
    }

    private boolean containsAny(String haystack, String... needles) {
        if (haystack == null) {
            return false;
        }
        for (String needle : needles) {
            if (needle != null && haystack.contains(needle)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAny(String haystack, List<String> needles) {
        if (haystack == null || needles == null) {
            return false;
        }
        for (String needle : needles) {
            if (haystack.contains(needle)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasOrderIdLikeText(String normalizedQuestion) {
        return normalizedQuestion != null && normalizedQuestion.matches(".*[a-z0-9]{8,}.*");
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('\u0111', 'd')
                .replace('\u0110', 'D');
        return normalized.toLowerCase(Locale.ROOT).trim();
    }

    private String safe(String value) {
        if (value == null || value.isBlank()) {
            return "N/A";
        }
        return value.replace("\n", " ").replace("\r", " ").trim();
    }

    private String shortText(String value, int maxLength) {
        String safe = safe(value);
        if (safe.length() <= maxLength) {
            return safe;
        }
        return safe.substring(0, maxLength - 3) + "...";
    }

    private String shortId(String value) {
        if (value == null) {
            return "N/A";
        }
        return value.substring(0, Math.min(8, value.length()));
    }

    private String orderStatus(Integer status) {
        if (status == null) {
            return "unknown";
        }
        return switch (status) {
            case 0 -> "pending";
            case 1 -> "confirmed";
            case 2 -> "shipping";
            case 3 -> "delivered";
            case 4 -> "completed";
            case 5 -> "cancelled";
            default -> "unknown";
        };
    }

    private String paymentStatus(Integer status) {
        if (status == null) {
            return "unknown";
        }
        return switch (status) {
            case 0 -> "unpaid";
            case 1 -> "paid";
            case 2 -> "failed";
            default -> "unknown";
        };
    }
}

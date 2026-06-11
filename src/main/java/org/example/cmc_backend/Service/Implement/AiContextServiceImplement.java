package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.*;
import org.example.cmc_backend.Models.DTO.AiContextResult;
import org.example.cmc_backend.Repository.*;
import org.example.cmc_backend.Service.AiContextService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AiContextServiceImplement implements AiContextService {
    private static final int MAX_PRODUCTS = 8;
    private static final int MAX_CATEGORIES = 8;
    private static final int MAX_CARE = 8;
    private static final int MAX_COUPONS = 5;
    private static final int MAX_ORDERS = 5;
    private static final List<String> AI_ENTITIES = List.of(
            "Movies",
            "Category",
            "Branchs",
            "Schedules",
            "Drinks",
            "Foods",
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
    private final DrinkRepository drinkRepository;
    private final FoodRepository foodRepository;
    private final VoucherRepository voucherRepository;
    private final BillRepository billRepository;

    public AiContextServiceImplement(
            MovieRepository movieRepository,
            CategoryRepository categoryRepository,
            BranchRepository branchRepository,
            ScheduleRepository scheduleRepository,
            DrinkRepository drinkRepository,
            FoodRepository foodRepository,
            VoucherRepository voucherRepository,
            BillRepository billRepository
    ) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
        this.branchRepository = branchRepository;
        this.scheduleRepository = scheduleRepository;
        this.drinkRepository = drinkRepository;
        this.foodRepository = foodRepository;
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
        List<BillEntity> billEntities = findBill(user, intent, terms);
        List<VoucherEntity> voucherEntities = findVoucher(intent, terms);
        List<FoodEntity> foodEntities = findFood(intent);
        List<DrinkEntity> drinkEntities = findDrink(intent);

        StringBuilder context = new StringBuilder();
        List<String> summary = new ArrayList<>();
        appendBillContext(context, summary, billEntities);
        appendCategoryContext(context, summary, categoryEntities);
        appendMovieContext(context, summary, categoryEntities);
        appendVoucherContext(context, summary, voucherEntities);
        appendFoodContext(context, summary, foodEntities);
        appendDrinkContext(context, summary, drinkEntities);

        int totalRecords = categoryEntities.size()  + voucherEntities.size() + billEntities.size();
        return new AiContextResult(intent, AI_ENTITIES, totalRecords, context.toString().trim(), summary);
    }

    private String detectIntent(String normalizedQuestion) {
        if (normalizedQuestion == null || normalizedQuestion.isBlank()) {
            return "OUT_OF_SCOPE";
        }
        if (containsAny(normalizedQuestion, "don hang", "bill", "thanh toan", "huy don")) {
            return "BILL";
        }
        if (containsAny(normalizedQuestion, "khuyen mai", "giam gia", "voucher", "ma giam", "uu dai")) {
            return "VOUCHER";
        }
        if (containsAny(normalizedQuestion, "gia", "bao nhieu", "tien", "re", "dat")) {
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
        if (containsAny(
                normalizedQuestion,
                "nuoc",
                "uong",
                "thuc"
        )) {
            return "DRINK";
        }

        if (containsAny(
                normalizedQuestion,
                "an",
                "thuc"
        )) {
            return "FOOD";
        }

        return "OUT_OF_SCOPE";
    }

    private List<MovieEntity> findMovies(String intent, List<String> terms) {
        boolean movieIntent = List.of("MOVIE").contains(intent);
        if (!movieIntent) {
            return List.of();
        }
        List<String> type_price = "VOUCHER".equals(intent) ? nonGenericPriceTerms(terms) : terms;

        List<MovieEntity> movieEntities = movieRepository.findAll().stream()
                .filter(movieEntity -> movieEntity.isShowing() == true)
                .limit(MAX_PRODUCTS)
                .toList();
        if (movieEntities.isEmpty()) {
            return movieRepository.findAll().stream()
                    .filter(movieEntity -> movieEntity.isShowing() == true)
                    .limit(MAX_PRODUCTS)
                    .toList();
        }
        return movieEntities;
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
        if (!List.of("VOUCHER").contains(intent)) {
            return List.of();
        }
        return voucherRepository.findAll().stream()
                .filter(voucherEntity -> voucherEntity.getCode() != null && voucherEntity.getExpiration() == null && voucherEntity.getDiscount() == null && voucherEntity.getQuality() == null)
                .filter(voucherEntity -> terms.isEmpty())
                .limit(MAX_CARE)
                .toList();
    }

    private List<BillEntity> findBill(UserEntity user,String intent, List<String> terms) {
        if (!List.of("BILL").contains(intent)|| user == null || user.getIdUser() == null) {
            return List.of();
        }
        return billRepository.findAllByUserEntity(user).stream()
                .filter(billEntity -> billEntity.getIdBill() != null)
                .filter(billEntity -> billEntity.getTotalAmount() != null && billEntity.getTotalAmount() > 0)
                .filter(billEntity -> billEntity.getBranchEntity() != null)
                .filter(billEntity -> billEntity.getVoucherEntity() != null)
                .filter(billEntity -> billEntity.getUserEntity() != null)
                .filter(billEntity -> billEntity.getBillDrinkDetailEntities().isEmpty())
                .filter(billEntity -> billEntity.getBillFoodDetailEntities().isEmpty())
                .filter(billEntity -> billEntity.getTicketEntities().isEmpty())
                .limit(MAX_COUPONS)
                .toList();
    }

    private List<DrinkEntity> findDrink( String intent) {
        if (!List.of("DRINK").contains(intent)) {
            return List.of();
        }
        return drinkRepository.findAll().stream()
                .filter(drinkEntity -> drinkEntity.getId() != null)
                .filter(drinkEntity -> drinkEntity.getName() != null)
                .filter(drinkEntity -> drinkEntity.getSizeDrinkEntities().isEmpty())
                .limit(MAX_ORDERS)
                .toList();
    }

    private List<FoodEntity> findFood( String intent) {
        if (!List.of("FOOD").contains(intent)) {
            return List.of();
        }
        return foodRepository.findAll().stream()
                .filter(foodEntity -> foodEntity.getIdFood() != null)
                .filter(foodEntity -> foodEntity.getName() != null)
                .filter(foodEntity -> foodEntity.getSizeFoodEntities().isEmpty())
                .limit(MAX_ORDERS)
                .toList();
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

    private void appendMovieContext(StringBuilder context, List<String> summary, List<CategoryEntity> categoryEntities) {
        if (categoryEntities.isEmpty()) {
            return;
        }
        context.append("Products:\n");
        for (CategoryEntity categoryEntity : categoryEntities) {
            List<MovieEntity> movieEntities = categoryEntity.getMovieEntities();
            for (MovieEntity movieEntity : movieEntities) {
                CategoryEntity category = movieEntity == null ? null : movieEntity.getCategoryEntity();
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
                summary.add("Movie" + safe(movieEntity.getNameMovie()));
            }
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

    private void appendBillContext(StringBuilder context, List<String> summary, List<BillEntity> billEntities) {
        if (billEntities.isEmpty()) {
            return;
        }
        context.append("Bills:\n");
        for (BillEntity billEntity : billEntities) {
            BranchEntity branchEntity = billEntity.getBranchEntity();
            context.append("- idBranch: ").append(safe(branchEntity.getIdBranch().toString())).append("\n");
            context.append("  address: ").append(safe(branchEntity == null ? null : branchEntity.getAddress())).append("\n");
            context.append("  nameBranch: ").append(safe(branchEntity == null ? null : branchEntity.getNameBranch())).append("\n");
            context.append("  phone: ").append(safe(branchEntity.getPhone())).append("\n");
            summary.add("Branch " + safe(branchEntity == null ? null : branchEntity.getNameBranch()) + " - address " + branchEntity.getAddress());
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
            summary.add("Voucher " + safe(voucherEntity.getCode()));
        }
    }

    private void appendDrinkContext(StringBuilder context, List<String> summary, List<DrinkEntity> drinkEntities) {
        if (drinkEntities.isEmpty()) {
            return;
        }
        context.append("Drink:\n");
        for (DrinkEntity drinkEntity : drinkEntities) {
            context.append("- id: ").append(safe(drinkEntity.getId().toString())).append("\n");
            context.append("  name: ").append(drinkEntity.getName()).append(" (").append(")\n");
            summary.add("Drink " + shortId(drinkEntity.getId().toString()) + " - " + drinkEntity.getName());
        }
    }

    private void appendFoodContext(StringBuilder context, List<String> summary, List<FoodEntity> foodEntities) {
        if (foodEntities.isEmpty()) {
            return;
        }
        context.append("Food:\n");
        for (FoodEntity foodEntity : foodEntities) {
            context.append("- id: ").append(safe(foodEntity.getIdFood().toString())).append("\n");
            context.append("  name: ").append(foodEntity.getName()).append(" (").append(")\n");
            summary.add("Food " + shortId(foodEntity.getIdFood().toString()) + " - " + foodEntity.getName());
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

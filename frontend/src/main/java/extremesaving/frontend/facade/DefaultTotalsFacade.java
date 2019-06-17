package extremesaving.frontend.facade;

import extremesaving.backend.dao.DataDao;
import extremesaving.backend.dto.AccountDto;
import extremesaving.backend.dto.CategoryDto;
import extremesaving.backend.dto.TotalsDto;
import extremesaving.backend.model.DataModel;
import extremesaving.backend.service.CalculationService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component("defaultTotalsFacade")
public class DefaultTotalsFacade implements TotalsFacade {

    @Resource(name = "defaultAccountFacade")
    private AccountFacade accountFacade;

    @Resource(name = "defaultCategoryFacade")
    private CategoryFacade categoryFacade;

    @Resource(name = "defaultCalculationService")
    private CalculationService calculationService ;

    @Resource(name = "defaultDataDao")
    private DataDao dataDao ;

    @Override
    public TotalsDto getTotals() {
        List<DataModel> dataModels = dataDao.findAll();
        Set<DataModel> nonTransferDataModels = dataModels.stream().filter(dataModel -> !dataModel.getCategory().isTransfer()).collect(Collectors.toSet());
        TotalsDto totalsDto = new TotalsDto();
        totalsDto.setData(new HashSet<>(dataModels));
        totalsDto.setTotalResults(calculationService.getResults(dataModels));
        totalsDto.setNoTransferResults(calculationService.getResults(nonTransferDataModels));
        totalsDto.setAccountDtoList(accountFacade.getAccounts());
        totalsDto.setCategoryDtoList(categoryFacade.getCategories());
        totalsDto.setMonthlyResults(calculationService.getMonthlyResults(dataModels));
        totalsDto.setYearlyResults(calculationService.getYearlyResults(dataModels));
        totalsDto.setYearlyNonTransferResults(calculationService.getYearlyResults(nonTransferDataModels));
        totalsDto.setYearPredictions(calculationService.getYearPredictions(dataModels));


//        totalsDto.setSurvivalDays(totalsDto.getTotalResults().getResult().divide(totalsDto.getAverageDailyExpense().multiply(BigDecimal.valueOf(-1)), RoundingMode.HALF_DOWN).longValue());

        Optional<CategoryDto> mostProfitableCategory = totalsDto.getCategoryDtoList().stream().max(Comparator.comparing(CategoryDto::getTotalIncomes));
        totalsDto.setMostProfitableCategory(mostProfitableCategory.isPresent() ? mostProfitableCategory.get() : null);

        Optional<CategoryDto> mostExpensiveCategory = totalsDto.getCategoryDtoList().stream().min(Comparator.comparing(CategoryDto::getTotalExpenses));
        totalsDto.setMostExpensiveCategory(mostExpensiveCategory.isPresent() ? mostExpensiveCategory.get() : null);

        Optional<AccountDto> mostProfitableAccount = totalsDto.getAccountDtoList().stream().max(Comparator.comparing(AccountDto::getTotalIncomes));
        totalsDto.setMostProfitableAccount(mostProfitableAccount.isPresent() ? mostProfitableAccount.get() : null);

        Optional<AccountDto> mostExpensiveAccount = totalsDto.getAccountDtoList().stream().min(Comparator.comparing(AccountDto::getTotalExpenses));
        totalsDto.setMostExpensiveAccount(mostExpensiveAccount.isPresent() ? mostExpensiveAccount.get() : null);

        return totalsDto;
    }
}
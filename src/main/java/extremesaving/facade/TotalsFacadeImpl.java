package extremesaving.facade;

import extremesaving.dto.AccountDto;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.TotalsDto;
import extremesaving.model.DataModel;
import extremesaving.service.AccountService;
import extremesaving.service.CalculationService;

import java.util.*;
import java.util.stream.Collectors;

public class TotalsFacadeImpl implements TotalsFacade {

    private CalculationService calculationService;

    @Override
    public TotalsDto getTotals() {
//        List<DataModel> dataModels = dataDao.findAll();
//        Set<DataModel> nonTransferDataModels = dataModels.stream().filter(dataModel -> !dataModel.getCategory().isTransfer()).collect(Collectors.toSet());
        TotalsDto totalsDto = new TotalsDto();


//        totalsDto.setData(new HashSet<>(dataModels));
//        totalsDto.setTotalResults(calculationService.getResults(dataModels));
//        totalsDto.setNoTransferResults(calculationService.getResults(nonTransferDataModels));
//        totalsDto.setAccountDtoList(accountFacade.getAccounts());
//        totalsDto.setCategoryDtoList(categoryFacade.getCategories());
//        totalsDto.setYearlyNonTransferResults(calculationService.getYearlyResults(nonTransferDataModels));
//        totalsDto.setYearPredictions(calculationService.getYearPredictions(dataModels));
//
//
////        totalsDto.setSurvivalDays(totalsDto.getTotalResults().getResult().divide(totalsDto.getAverageDailyExpense().multiply(BigDecimal.valueOf(-1)), RoundingMode.HALF_DOWN).longValue());
//
//        Optional<CategoryDto> mostProfitableCategory = totalsDto.getCategoryDtoList().stream().max(Comparator.comparing(CategoryDto::getTotalIncomes));
//        totalsDto.setMostProfitableCategory(mostProfitableCategory.isPresent() ? mostProfitableCategory.get() : null);
//
//        Optional<CategoryDto> mostExpensiveCategory = totalsDto.getCategoryDtoList().stream().min(Comparator.comparing(CategoryDto::getTotalExpenses));
//        totalsDto.setMostExpensiveCategory(mostExpensiveCategory.isPresent() ? mostExpensiveCategory.get() : null);
//
//        Optional<AccountDto> mostProfitableAccount = totalsDto.getAccountDtoList().stream().max(Comparator.comparing(AccountDto::getTotalIncomes));
//        totalsDto.setMostProfitableAccount(mostProfitableAccount.isPresent() ? mostProfitableAccount.get() : null);
//
//        Optional<AccountDto> mostExpensiveAccount = totalsDto.getAccountDtoList().stream().min(Comparator.comparing(AccountDto::getTotalExpenses));
//        totalsDto.setMostExpensiveAccount(mostExpensiveAccount.isPresent() ? mostExpensiveAccount.get() : null);

        return totalsDto;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

}
package facade;

import dao.AccountDao;
import dao.DataDao;
import dao.DefaultAccountDao;
import dao.DefaultDataDao;
import dto.AccountDto;
import model.AccountModel;
import model.DataModel;
import service.CalculationService;
import service.DefaultCalculationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultAccountFacade implements AccountFacade {

    private static AccountDao accountDao = new DefaultAccountDao();
    private static CalculationService calculationService = new DefaultCalculationService();

    private static DataDao dataDao = new DefaultDataDao();

    @Override
    public List<AccountDto> getAccounts() {
        List<AccountDto> accountDtos = new ArrayList<>();
        List<AccountModel> accountModels = accountDao.findAll();
        for (AccountModel accountModel : accountModels) {
            accountDtos.add(convertAccountModelToAccountDto(accountModel));
        }
        Collections.sort(accountDtos, Comparator.comparing(AccountDto::getAccountName));
        return accountDtos;
    }

    private AccountDto convertAccountModelToAccountDto(AccountModel accountModel) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(accountModel.getId());
        accountDto.setAccountName(accountModel.getName());

        List<DataModel> accountResults = dataDao.findByAccount(accountModel.getId());
        accountDto.setTotalResults(calculationService.getResults(accountResults));
        accountDto.setNonTransferResults(calculationService.getResults(accountResults.stream().filter(data -> !data.getCategory().isTransfer()).collect(Collectors.toList())));
        return accountDto;
    }
}
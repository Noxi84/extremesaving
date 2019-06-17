package extremesaving.frontend.facade;

import extremesaving.backend.dao.AccountDao;
import extremesaving.backend.dao.DataDao;
import extremesaving.backend.model.AccountModel;
import extremesaving.backend.model.DataModel;
import extremesaving.backend.service.CalculationService;
import extremesaving.backend.dto.AccountDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("defaultAccountFacade")
public class DefaultAccountFacade implements AccountFacade {

    @Resource(name = "defaultAccountDao")
    private AccountDao accountDao;

    @Resource(name = "defaultCalculationService")
    private CalculationService calculationService;

    @Resource(name = "defaultDataDao")
    private DataDao dataDao;

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
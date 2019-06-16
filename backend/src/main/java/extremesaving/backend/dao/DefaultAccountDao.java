package extremesaving.backend.dao;

import extremesaving.backend.model.AccountModel;
import extremesaving.util.csvparser.AccountCsvParser;
import extremesaving.util.csvparser.data.AccountCsv;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("defaultAccountDao")
public class DefaultAccountDao implements AccountDao {

    private AccountCsvParser accountCsvParser = new AccountCsvParser();

    @Override
    public List<AccountModel> findAll() {
        List<AccountCsv> csvList = accountCsvParser.parseCsv();
        List<AccountModel> accoutModels = new ArrayList<>();
        for (AccountCsv csv : csvList) {
            accoutModels.add(convert(csv));
        }
        return accoutModels;
    }

    @Override
    public Optional<AccountModel> getAccountById(String id) {
        List<AccountModel> accounts = findAll();
        return accounts.stream().filter(account -> account.getId().equals(id)).findFirst();
    }

    private AccountModel convert(AccountCsv accountCsv) {
        AccountModel accountModel = new AccountModel();
        accountModel.setId(accountCsv.getId());
        accountModel.setName(accountCsv.getName());
        return accountModel;
    }

    @Override
    public boolean saveOrUpdateAccount(String id, String name) {
        return false;
    }

    @Override
    public boolean removeAccount(String id) {
        return false;
    }
}

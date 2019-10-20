package extremesaving.service;

import extremesaving.data.service.DataService;
import extremesaving.dto.AccountDto;
import extremesaving.data.model.DataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {

    private DataService dataService;
    private CalculationService calculationService;

    @Override
    public List<AccountDto> getAccounts() {
        List<DataModel> dataModels = dataService.findAll();

        List<String> accounts = new ArrayList<>(dataModels.stream().map(dataModel -> dataModel.getAccount()).collect(Collectors.toSet()));
        Collections.sort(accounts);

        List<AccountDto> accountDtos = new ArrayList<>();
        for (String account : accounts) {
            AccountDto accountDto = new AccountDto();
            accountDto.setName(account);
            accountDto.setTotalResults(calculationService.getResults(dataModels.stream().filter(dataModel -> dataModel.getAccount().equals(account)).collect(Collectors.toList())));
            accountDtos.add(accountDto);
        }
        return accountDtos;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}
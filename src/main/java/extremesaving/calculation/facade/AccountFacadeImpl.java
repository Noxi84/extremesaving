package extremesaving.calculation.facade;

import extremesaving.calculation.dto.AccountDto;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AccountFacadeImpl implements AccountFacade {

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;

    @Override
    public List<AccountDto> getAccounts() {
        List<DataDto> dataDtos = dataFacade.findAll();

        List<String> accounts = new ArrayList<>(dataDtos.stream().map(dataDto -> dataDto.getAccount()).collect(Collectors.toSet()));
        Collections.sort(accounts);

        List<AccountDto> accountDtos = new ArrayList<>();
        for (String account : accounts) {
            AccountDto accountDto = new AccountDto();
            accountDto.setName(account);
            accountDto.setTotalResults(calculationFacade.getResults(dataDtos.stream().filter(dataDto -> dataDto.getAccount().equals(account)).collect(Collectors.toList())));
            accountDtos.add(accountDto);
        }
        return accountDtos;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}
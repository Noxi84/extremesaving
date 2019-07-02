package extremesaving.service;

import extremesaving.dto.AccountDto;

import java.util.List;

public interface AccountService {

    List<AccountDto> getAccounts();
}
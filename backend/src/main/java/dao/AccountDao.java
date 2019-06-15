package dao;

import model.AccountModel;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    List<AccountModel> findAll();

    Optional<AccountModel> getAccountById(String id);

    boolean saveOrUpdateAccount(String id, String name);

    boolean removeAccount(String id);
}

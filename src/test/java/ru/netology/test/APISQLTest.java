package ru.netology.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APISQLTest {

    @Test
    public void validTransferFromFirsToSecond() {
        var authInfo = DataHelper.getAuthInfoTest();
        APIHelper.makeQueryToLogin(authInfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authInfo.getLogin(), verificationCode.getCode());
        var tokenInfo = APIHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.getToken(), 200);
        var firstCardBalance = cardsBalances.get(DataHelper.getFirstCard().getId());
        var secondCardBalance = cardsBalances.get(DataHelper.getSecondCard().getId());
        var amount = DataHelper.generateValidAmount(firstCardBalance);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getFirstCard().getNumber(), DataHelper.getSecondCard().getNumber(), amount);
        APIHelper.generateQueryToTransfer(tokenInfo.getToken(), transferInfo, 200);
        cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.getToken(), 200);
        var actualFirstCardBalance = cardsBalances.get(DataHelper.getFirstCard().getId());
        var actualSecondCardBalance = cardsBalances.get(DataHelper.getSecondCard().getId());
        assertAll(() -> assertEquals(firstCardBalance - amount, actualFirstCardBalance),
                () -> assertEquals(secondCardBalance + amount, actualSecondCardBalance));
    }

}

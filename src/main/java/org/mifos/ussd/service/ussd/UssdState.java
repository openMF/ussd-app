package org.mifos.ussd.service.ussd;

public enum UssdState {
    INITIAL_STATE,
    MAIN_MENU_STATE,
    ENTER_PIN_STATE,
    TRANSFER_MENU_STATE,
    WITHDRAW_MENU_STATE,
    WITHDRAW_STATE,
    CANCEL_WITHDRAW_STATE,
    LOAN_MENU_STATE,
    SAVINGS_MENU_STATE,
    ENTER_WITHDRAW_AMOUNT_STATE,
    CONFIRM_WITHDRAW_AMOUNT_STATE,
    END_STATE
}

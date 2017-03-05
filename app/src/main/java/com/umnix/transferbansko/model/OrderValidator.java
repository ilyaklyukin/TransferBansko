package com.umnix.transferbansko.model;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import com.umnix.transferbansko.R;
import com.umnix.transferbansko.TransferApplication;

import javax.inject.Inject;

public class OrderValidator {

    @Inject
    protected Context context;

    public OrderValidator() {
        TransferApplication.getComponent().inject(this);
    }

    /**
     * Check all fields in the order of controls
     *
     * @return Description of the first invalid input data.
     * NULL if all data is valid.
     */
    public String getNextError(Order order) {
        if (!isDestinationValid(order)) {
            return context.getString(R.string.invalid_destination);
        }

        if (!isTransportTypeValid(order)) {
            return context.getString(R.string.invalid_transport_type);
        }

        if (!isTimeSet(order)) {
            return context.getString(R.string.invalid_dates);
        }

        if (!isArrivalDateTimeValid(order)) {
            return context.getString(R.string.invalid_arrival_time);
        }

        if (!isDepartureDateTimeValid(order)) {
            return context.getString(R.string.invalid_departure_time);
        }

        if (!isPassengersCountValid(order)) {
            return context.getString(R.string.invalid_passengers_count);
        }

        if (!isChildCountValid(order)) {
            return context.getString(R.string.invalid_child_seats_count);
        }

        if (!isClientNameValid(order)) {
            return context.getString(R.string.invalid_name);
        }

        if (!isClientEmailValid(order)) {
            return context.getString(R.string.invalid_email);
        }

        if (!isClientPhoneValid(order)) {
            return context.getString(R.string.invalid_phone);
        }

        return null;
    }

    private boolean isArrivalDateTimeValid(Order order) {
        return TextUtils.isEmpty(order.getArrivalDate()) == TextUtils.isEmpty(order.getArrivalTime());
    }


    private boolean isDepartureDateTimeValid(Order order) {
        return TextUtils.isEmpty(order.getDepartureDate()) == TextUtils.isEmpty(order.getDepartureTime());
    }

    private boolean isDestinationValid(Order order) {
        return order.getDestination() != null;
    }

    private boolean isTransportTypeValid(Order order) {
        return order.getTransportType() != null;
    }

    private boolean isTimeSet(Order order) {
        boolean isEmptyDeparture = TextUtils.isEmpty(order.getDepartureDate()) ||
                TextUtils.isEmpty(order.getDepartureTime());
        boolean isEmptyArrival = TextUtils.isEmpty(order.getArrivalDate()) ||
                TextUtils.isEmpty(order.getArrivalTime());

        return !isEmptyDeparture || !isEmptyArrival;
    }

    private boolean isPassengersCountValid(Order order) {
        if (TextUtils.isEmpty(order.getPassengersCount())) {
            return false;
        }

        try {
            int count = Integer.valueOf(order.getPassengersCount());
            return count > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isChildCountValid(Order order) {
        if (TextUtils.isEmpty(order.getChildCount())) {
            return true;
        }

        try {
            int count = Integer.valueOf(order.getChildCount());
            return count >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isClientNameValid(Order order) {
        return !TextUtils.isEmpty(order.getClientName());
    }

    private boolean isClientEmailValid(Order order) {
        if (TextUtils.isEmpty(order.getClientEmail())) {
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(order.getClientEmail()).matches();
    }

    private boolean isClientPhoneValid(Order order) {
        if (TextUtils.isEmpty(order.getClientPhone())) {
            return false;
        }

        return PhoneNumberUtils.isGlobalPhoneNumber(order.getClientPhone());
    }
}

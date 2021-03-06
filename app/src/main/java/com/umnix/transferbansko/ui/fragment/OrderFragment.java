package com.umnix.transferbansko.ui.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.umnix.transferbansko.R;
import com.umnix.transferbansko.TransferApplication;
import com.umnix.transferbansko.model.Destination;
import com.umnix.transferbansko.model.Order;
import com.umnix.transferbansko.model.TransportType;
import com.umnix.transferbansko.utils.ViewUtility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class OrderFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.date_input)
    protected EditText dateInput;

    @BindView(R.id.time_input)
    protected EditText timeInput;

    @BindView(R.id.child_input)
    protected EditText childCountInput;

    @BindView(R.id.passengers_input)
    protected EditText passengerCountInput;

    @BindView(R.id.name_input)
    protected EditText nameInput;

    @BindView(R.id.hotel_input)
    protected EditText hotelInput;

    @BindView(R.id.email_input)
    protected EditText emailInput;

    @BindView(R.id.phone_input)
    protected EditText phoneInput;

    @BindView(R.id.flight_input)
    protected EditText flightInput;

    @BindView(R.id.return_transfer)
    protected CheckBox returnTransferInput;

    @BindView(R.id.mpv_choice)
    protected RadioButton mpvCarSelection;

    @BindView(R.id.minibus_choice)
    protected RadioButton miniBusSelection;

    @BindView(R.id.arrival_choice)
    protected RadioButton arrivalSelection;

    @BindView(R.id.departure_choice)
    protected RadioButton departureSelection;

    @BindView(R.id.total_amount_value)
    protected TextView totalAmountValue;

    @BindView(R.id.clear_date)
    protected ImageView clearDate;

    @BindView(R.id.comments_input)
    protected EditText commentsInput;

    @BindView(R.id.destination_group)
    protected RadioGroup destinationGroup;

    @BindView(R.id.order_scroll)
    protected ScrollView orderScroll;

    private Calendar calendar = Calendar.getInstance();

    private Order order = new Order();

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_order, container, false);

        TransferApplication.getComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);

        animateFadeIn(view);

        initControls();

        return view;
    }

    private void initControls() {
        getFilledOrder();
        totalAmountValue.setText(order.calcTotalAmount());

       /* orderScroll.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> {
                    if (orderScroll != null) {
                        orderScroll.post(() -> {
                                    if (orderScroll != null) {
                                        orderScroll.fullScroll(View.FOCUS_UP);
                                    }
                                }
                        );
                    }
                });*/
    }

    @OnClick(R.id.dest_sofia_bansko_choice)
    protected void onDestination1Select() {
        getFilledOrder();

        totalAmountValue.setText(order.calcTotalAmount());
    }

    @OnClick(R.id.dest_bansko_sofia_choice)
    protected void onDestination2Select() {
        getFilledOrder();

        totalAmountValue.setText(order.calcTotalAmount());
    }

    @OnClick(R.id.dest_plovdiv_bansko_choice)
    protected void onDestination3Select() {
        getFilledOrder();

        totalAmountValue.setText(order.calcTotalAmount());
    }

    @OnClick(R.id.dest_bansko_plovdiv_choice)
    protected void onDestination4Select() {
        getFilledOrder();

        totalAmountValue.setText(order.calcTotalAmount());
    }

    @OnClick(R.id.dest_thessaloniki_bansko_choice)
    protected void onDestination5Select() {
        getFilledOrder();

        totalAmountValue.setText(order.calcTotalAmount());
    }

    @OnClick(R.id.dest_bansko_thessaloniki_choice)
    protected void onDestination6Select() {
        getFilledOrder();

        totalAmountValue.setText(order.calcTotalAmount());
    }

    @OnClick(R.id.dest_different_choice)
    protected void onDestination7Select() {
        getFilledOrder();

        totalAmountValue.setText(order.calcTotalAmount());
    }

    private void setDate() {
        String format = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);

        String date = sdf.format(calendar.getTime());
        dateInput.setText(date);

        ViewUtility.setVisibility(View.VISIBLE, clearDate);

        if (arrivalSelection.isChecked()) {
            order.setArrivalDate(date);
        } else if (departureSelection.isChecked()) {
            order.setDepartureDate(date);
        }
    }

    private void setTime() {
        String format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);

        String time = sdf.format(calendar.getTime());
        timeInput.setText(time);

        ViewUtility.setVisibility(View.VISIBLE, clearDate);

        if (arrivalSelection.isChecked()) {
            order.setArrivalTime(time);
        } else if (departureSelection.isChecked()) {
            order.setDepartureTime(time);
        }
    }

    @OnClick(R.id.arrival_choice)
    protected void onArrivalSelect() {
        dateInput.setText(order.getArrivalDate());
        timeInput.setText(order.getArrivalTime());
        flightInput.setText(order.getArrivalFlight());

        ViewUtility.setVisibility(isFlightInfoAdded() ? View.VISIBLE : View.GONE, clearDate);
    }

    @OnClick(R.id.departure_choice)
    protected void onDepartureSelect() {
        dateInput.setText(order.getDepartureDate());
        timeInput.setText(order.getDepartureTime());
        flightInput.setText(order.getDepartureFlight());

        ViewUtility.setVisibility(isFlightInfoAdded() ? View.VISIBLE : View.GONE, clearDate);
    }

    private boolean isFlightInfoAdded() {
        String flight = flightInput.getText().toString();
        String date = dateInput.getText().toString();
        String time = timeInput.getText().toString();

        return !TextUtils.isEmpty(flight) || !TextUtils.isEmpty(date) || !TextUtils.isEmpty(time);
    }

    @OnTextChanged(R.id.flight_input)
    protected void onFlightEdit() {
        ViewUtility.setVisibility(isFlightInfoAdded() ? View.VISIBLE : View.GONE, clearDate);

        String flight = flightInput.getText().toString();
        if (arrivalSelection.isChecked()) {
            order.setArrivalFlight(flight);
        } else if (departureSelection.isChecked()) {
            order.setDepartureFlight(flight);
        }
    }

    @OnClick(R.id.clear_date)
    protected void onDateClear() {
        if (arrivalSelection.isChecked()) {
            order.setArrivalFlight(null);
            order.setArrivalDate(null);
            order.setArrivalTime(null);
            flightInput.setText("");
            dateInput.setText("");
            timeInput.setText("");
        } else if (departureSelection.isChecked()) {
            order.setDepartureFlight(null);
            order.setDepartureDate(null);
            order.setDepartureTime(null);
            flightInput.setText("");
            dateInput.setText("");
            timeInput.setText("");
        }

        ViewUtility.setVisibility(View.GONE, clearDate);
    }

    @OnClick(R.id.mpv_choice)
    protected void onMPVSelect() {
        order.setTransportType(TransportType.MPV);
        totalAmountValue.setText(order.calcTotalAmount());
    }


    @OnClick(R.id.minibus_choice)
    protected void onMiniBusSelect() {
        order.setTransportType(TransportType.MINI_BUS);
        totalAmountValue.setText(order.calcTotalAmount());
    }

    @OnClick(R.id.return_transfer)
    protected void onChangeNeedReturn() {
        order.setNeedReturn(returnTransferInput.isChecked());
        totalAmountValue.setText(order.calcTotalAmount());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        setDate();
    }

    @OnClick(R.id.date_input)
    protected void onDateClick() {
        new DatePickerDialog(getActivity(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnFocusChange(R.id.date_input)
    protected void onDateFocus(boolean hasFocus) {
        if (!hasFocus){
            return;
        }
        onDateClick();
    }

    @OnClick(R.id.time_input)
    protected void onTimeClick() {
        new TimePickerDialog(getActivity(),
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }

    @OnFocusChange(R.id.time_input)
    protected void onTimeFocus(boolean hasFocus) {
        if (!hasFocus){
            return;
        }
        onTimeClick();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        setTime();
    }

    public Order getFilledOrder() {

        int radioButtonID = destinationGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) destinationGroup.findViewById(radioButtonID);
        String destination = radioButton.getText().toString();

        order.setDestination(Destination.fromDescription(destination));

        order.setPassengersCount(passengerCountInput.getText().toString());
        order.setChildCount(childCountInput.getText().toString());

        order.setNeedReturn(returnTransferInput.isChecked());

        order.setClientName(nameInput.getText().toString());
        order.setClientHotel(hotelInput.getText().toString());
        order.setClientEmail(emailInput.getText().toString());
        order.setClientPhone(phoneInput.getText().toString());

        order.setComments(commentsInput.getText().toString());

        if (mpvCarSelection.isChecked()) {
            order.setTransportType(TransportType.MPV);
        } else if (miniBusSelection.isChecked()) {
            order.setTransportType(TransportType.MINI_BUS);
        }

        return order;
    }
}

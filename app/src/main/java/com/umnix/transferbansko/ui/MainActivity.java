package com.umnix.transferbansko.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.umnix.transferbansko.R;
import com.umnix.transferbansko.TransferApplication;
import com.umnix.transferbansko.model.Order;
import com.umnix.transferbansko.model.OrderValidator;
import com.umnix.transferbansko.ui.fragment.InfoType;
import com.umnix.transferbansko.ui.fragment.OrderFragment;
import com.umnix.transferbansko.ui.fragment.TextFragment;
import com.umnix.transferbansko.utils.GMailSender;
import com.umnix.transferbansko.utils.ServiceBus;
import com.umnix.transferbansko.utils.ViewUtility;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    @Inject
    ServiceBus serviceBus;

    @Inject
    OrderValidator orderValidator;

    private CompositeDisposable subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        TransferApplication.getComponent().inject(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        addFragment();

        subscribeToEvents();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onDestroy() {
        if (subscriptions != null) {
            subscriptions.dispose();
            subscriptions = null;
        }
        super.onDestroy();
    }

    private void subscribeToEvents() {
        subscriptions = new CompositeDisposable();

        Disposable subscription = serviceBus.getSentEmailState().subscribe(
                isSent -> {
                    if (isSent) {
                        Snackbar.make(fab, R.string.sent_approve, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(fab, R.string.common_error, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                },
                throwable -> {
                    Snackbar.make(fab, R.string.common_error, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
        );

        subscriptions.add(subscription);
    }

    private void addFragment() {
        String tag = TextFragment.class.getName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            return;
        }

        tag = OrderFragment.class.getName();
        fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            return;
        }

        attachOrderFragment();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order) {
            attachOrderFragment();
        } else if (id == R.id.nav_info) {
            attachTextFragment(InfoType.INFO);
        } else if (id == R.id.nav_prices) {
            attachTextFragment(InfoType.PRICE);
        } else if (id == R.id.nav_contacts) {
            attachTextFragment(InfoType.CONTACTS);
        } else if (id == R.id.nav_terms) {
            attachTextFragment(InfoType.TERMS);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void attachOrderFragment() {
        ViewUtility.setVisibility(View.VISIBLE, fab);

        OrderFragment fragment = OrderFragment.newInstance();
        createFragment(fragment, false);

        toolbar.setTitle(getString(R.string.app_name));
    }

    private void attachTextFragment(InfoType infoType) {
        ViewUtility.setVisibility(View.GONE, fab);

        TextFragment fragment = TextFragment.newInstance(infoType);
        createFragment(fragment, false);

        toolbar.setTitle(infoType.getTitle());
    }

    private void createFragment(Fragment fragment, boolean isInStack) {
        ViewUtility.hideSoftKeyboard(this);

        String tag = fragment.getClass().getName();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (isInStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.content_main, fragment, tag);
        ft.commit();
    }

    @OnClick(R.id.fab)
    protected void onSendOrder() {

        if (!ViewUtility.isNetworkAvailable(this)) {
            Timber.d("No internet connection");
            Snackbar.make(fab, R.string.no_internet, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        String tag = OrderFragment.class.getName();
        OrderFragment fragment = (OrderFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            Timber.e("Try to send email without fragment");
            Snackbar.make(fab, R.string.common_error, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        Order order = fragment.getFilledOrder();
        if (order == null) {
            Timber.e("Try to send email without formed order");
            Snackbar.make(fab, R.string.common_error, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        String invalidFieldDescription = orderValidator.getNextError(order);
        if (!TextUtils.isEmpty(invalidFieldDescription)) {
            Timber.e(invalidFieldDescription);
            Snackbar.make(fab, invalidFieldDescription, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        sendEmail(order);
    }

    private void sendEmail() {

        Disposable subscription = Observable.defer(() -> Observable.just(null))
                .subscribeOn(Schedulers.io())

                .subscribe(
                        o -> {
                            try {
                                GMailSender sender = new GMailSender("ilya.klyukin@gmail.com", "masterkey1");
                                sender.sendMail("This is Subject",
                                        "This is Body",
                                        "test@gmail.com",
                                        "ilya.klyukin@gmail.com");

                                serviceBus.postSentEmailState(true);
                            } catch (Exception e) {
                                Timber.e(e, e.getLocalizedMessage());
                                serviceBus.postSentEmailState(false);
                            }
                        },
                        e -> {
                            serviceBus.postSentEmailState(false);
                            Timber.e(e, "Error on sending email");
                        }
                );
        subscriptions.add(subscription);
    }

    private boolean sendEmail(Order order) {
        Observable.defer(() -> Observable.just(order))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        o -> {
                            try {
                                GMailSender sender = new GMailSender(getString(R.string.sender_email),
                                        getString(R.string.email_password));

                                String orderContent = order.fillContent(getString(R.string.email_template));

                                sender.sendMail(getString(R.string.email_title),
                                        orderContent,
                                        order.getClientEmail(),
                                        getString(R.string.receiver_email) + "," + order.getClientEmail());

                                serviceBus.postSentEmailState(true);
                            } catch (Exception e) {
                                serviceBus.postSentEmailState(false);
                                Timber.e(e, e.getLocalizedMessage());
                            }
                        },
                        e -> {
                            Timber.e(e, "Error on sending email");
                            serviceBus.postSentEmailState(false);
                        }
                );

        ViewUtility.hideSoftKeyboard(this);

        return true;
    }
}

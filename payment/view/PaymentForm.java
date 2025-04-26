package payment.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.ModalBorderAction;
import raven.modal.component.SimpleModalBorder;

import javax.swing.*;

public class PaymentForm extends JPanel {

    public PaymentForm() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap 2,fillx,insets n 35 n 35", "[fill,200]"));

        JLabel lbContactDetail = new JLabel("Contact Details");
        lbContactDetail.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +2;");
        add(lbContactDetail, "gapy 10 10,span 2");

        add(new JLabel("Payment option type"), "span 2");
        JComboBox comboPaymentType = new JComboBox();
        comboPaymentType.addItem("Bank Transfer");
        comboPaymentType.addItem("Alipay");
        comboPaymentType.addItem("Apple Pay");
        comboPaymentType.addItem("Online Payment Gateways");
        comboPaymentType.addItem("Credit/Debit Card");

        add(comboPaymentType, "gapy n 5,span 2");

        add(new JLabel("Nom"));
        add(new JLabel("Email"));

        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Name");
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "example@mail.com");
        add(txtName);
        add(txtEmail);

        JLabel lbRequestDetail = new JLabel("Payment Details");
        lbRequestDetail.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +2;");
        add(lbRequestDetail, "gapy 10 10,span 2");

        add(new JLabel("Amount"));
        add(new JLabel("Secret Code"));

        JTextField txtAmount = new JTextField();
        JFormattedTextField dateEditor = new JFormattedTextField();


        txtAmount.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "0.00");
        JLabel lbDollar = new JLabel("$");
        lbDollar.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,8,0,0;");
        txtAmount.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, lbDollar);
        add(txtAmount);
        add(dateEditor);

        add(new JLabel("Bank Option (Optional)"), "gapy 4,span 2");
        JComboBox comboCompany = new JComboBox();
        comboCompany.addItem("Visa ");
        comboCompany.addItem("MasterCard");
        comboCompany.addItem("American Express");
        comboCompany.addItem("KakaoBank");

        add(comboCompany, "Span 2");

        add(new JLabel("Destination Address"), "gapy 5,span 2");
        JComboBox comboAccount = new JComboBox();
        comboAccount.addItem("15 rue schnapper, 78100");
        comboAccount.addItem("17 avenue Excelmans, 75016");
        comboAccount.addItem("112 Avenue de longchamps, 75016");
        comboAccount.addItem("81 boulevard Victor Hugo,95100");
        comboAccount.addItem("39 avenue G. de Gaulle, 69000");

        add(comboAccount, "Span 2");

        JTextArea textArea = new JTextArea();
        textArea.setEnabled(false);
        textArea.setText("Incoming payment are placed in a secure receiving account to keep\ndestination account details anonymous.");
        textArea.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;" +
                "font:-1;" +
                "background:null;");
        add(textArea, "gapy 5 5,span 2");

        JLabel lbAlerts = new JLabel("Payment link expires in 23h59min");
        lbAlerts.setIcon(new FlatSVGIcon("payment/icon/clock.svg"));
        lbAlerts.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:8,8,8,8;" +
                "arc:$Component.arc;" +
                "background:fade(#ADD8E6,10%);");
        add(lbAlerts, "gapy n 10,span 2");

        // action button

        JButton cmdCancel = new JButton("Cancel");
        JButton cmdPayment = new JButton("Proceed Payment") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdCancel.addActionListener(actionEvent -> {
            ModalBorderAction.getModalBorderAction(this).doAction(SimpleModalBorder.CANCEL_OPTION);
        });

        cmdPayment.addActionListener(actionEvent -> {
            ModalBorderAction.getModalBorderAction(this).doAction(SimpleModalBorder.OK_OPTION);
        });

        add(cmdCancel, "grow 0");
        add(cmdPayment, "grow 0, al trailing");
    }
}

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

//  ABSTRACTION — Interface
interface Bookable {
    void book();

    void cancel();

    String getBookingStatus();
}

// ABSTRACTION — Abstract base class
abstract class PhotoshootService {

    private String clientName;
    private String bookingDate;
    private int sessionDurationMinutes;
    private double basePrice;
    private String status;

    public PhotoshootService(String clientName, String bookingDate, int sessionDurationMinutes, double basePrice) {
        setClientName(clientName);
        setBookingDate(bookingDate);
        setSessionDurationMinutes(sessionDurationMinutes);
        setBasePrice(basePrice);
        this.status = "Pending";
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Client name cannot be empty.");
        this.clientName = name.trim();
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String date) {
        if (date == null || date.trim().isEmpty())
            throw new IllegalArgumentException("Booking date cannot be empty.");
        this.bookingDate = date.trim();
    }

    public int getSessionDurationMinutes() {
        return sessionDurationMinutes;
    }

    public void setSessionDurationMinutes(int m) {
        if (m <= 0)
            throw new IllegalArgumentException("Duration must be positive.");
        this.sessionDurationMinutes = m;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double price) {
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative.");
        this.basePrice = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract String getServiceType();

    public abstract double getExtensionRatePerHour();

    public abstract void displayServiceDetails();

    // POLYMORPHISM — overloading
    public double calculateTotal() {
        return basePrice;
    }

    public double calculateTotal(int extraHours) {
        if (extraHours < 0)
            throw new IllegalArgumentException("Extra hours cannot be negative.");
        return basePrice + (extraHours * getExtensionRatePerHour());
    }

    public String getReceiptText(int extraHours) {
        StringBuilder sb = new StringBuilder();
        sb.append("  G ChromaLens — Booking Receipt\n");
        sb.append("  ══════════════════════════════════════════\n");
        sb.append(String.format("  %-18s : %s%n", "Service", getServiceType()));
        sb.append(String.format("  %-18s : %s%n", "Client", clientName));
        sb.append(String.format("  %-18s : %s%n", "Date", bookingDate));
        sb.append(String.format("  %-18s : %s%n", "Duration", sessionDurationMinutes + " mins"));
        sb.append(String.format("  %-18s : %s%n", "Delivery", "Google Drive"));
        sb.append(String.format("  %-18s : %s%n", "Edited Photos", "Included"));
        sb.append(String.format("  %-18s : P%.2f%n", "Base Price", basePrice));
        if (extraHours > 0) {
            sb.append(String.format("  %-18s : %s%n", "Extra Hours", extraHours + " hr(s)"));
            sb.append(String.format("  %-18s : P%.2f%n", "Extension Fee", extraHours * getExtensionRatePerHour()));
        }
        sb.append("  ──────────────────────────────────────────\n");
        sb.append(String.format("  %-18s : P%.2f%n", "TOTAL", calculateTotal(extraHours)));
        sb.append(String.format("  %-18s : %s%n", "Status", status));
        sb.append("  ══════════════════════════════════════════\n");
        return sb.toString();
    }

    public void printReceipt(int extraHours) {
        System.out.print(getReceiptText(extraHours));
    }
}

// INHERITANCE — Subclass 1: Individual Portrait
class IndividualPortrait extends PhotoshootService {

    private String portraitSubtype;

    public IndividualPortrait(String clientName, String bookingDate, String subtype) {
        super(clientName, bookingDate, 60, 500.0);
        setPortraitSubtype(subtype);
    }

    public String getPortraitSubtype() {
        return portraitSubtype;
    }

    public void setPortraitSubtype(String s) {
        if (s == null || s.trim().isEmpty())
            throw new IllegalArgumentException("Subtype cannot be empty.");
        this.portraitSubtype = s.trim();
    }

    @Override
    public String getServiceType() {
        return "Individual Portrait (" + portraitSubtype + ")";
    }

    @Override
    public double getExtensionRatePerHour() {
        return 300.0;
    }

    @Override
    public void displayServiceDetails() {
        System.out.println("  Package  : Individual Portrait");
        System.out.println("  Subtype  : " + portraitSubtype);
        System.out.println("  Session  : 60 minutes | P500.00 | +P300/hr");
        System.out.println("  Delivery : Soft copies via Google Drive");
    }
}

// INHERITANCE — Subclass 2: Portrait
class Portrait extends PhotoshootService {

    private String occasion;
    private int numberOfSubjects;

    public Portrait(String clientName, String bookingDate, String occasion, int subjects) {
        super(clientName, bookingDate, 120, 1000.0);
        setOccasion(occasion);
        setNumberOfSubjects(subjects);
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String o) {
        if (o == null || o.trim().isEmpty())
            throw new IllegalArgumentException("Occasion cannot be empty.");
        this.occasion = o.trim();
    }

    public int getNumberOfSubjects() {
        return numberOfSubjects;
    }

    public void setNumberOfSubjects(int count) {
        if (count <= 0)
            throw new IllegalArgumentException("Subjects must be at least 1.");
        this.numberOfSubjects = count;
    }

    @Override
    public String getServiceType() {
        return "Portrait — " + occasion;
    }

    @Override
    public double getExtensionRatePerHour() {
        return 500.0;
    }

    @Override
    public void displayServiceDetails() {
        System.out.println("  Package  : Portrait");
        System.out.println("  Occasion : " + occasion + " | Subjects: " + numberOfSubjects);
        System.out.println("  Session  : 120 minutes | P1,000.00 | +P500/hr");
        System.out.println("  Delivery : Soft copies via Google Drive");
    }
}

// INHERITANCE — Subclass 3: Commercial
class Commercial extends PhotoshootService {

    private String commercialCategory;

    public Commercial(String clientName, String bookingDate, String category) {
        super(clientName, bookingDate, 120, 2000.0);
        setCommercialCategory(category);
    }

    public String getCommercialCategory() {
        return commercialCategory;
    }

    public void setCommercialCategory(String c) {
        if (c == null || c.trim().isEmpty())
            throw new IllegalArgumentException("Category cannot be empty.");
        this.commercialCategory = c.trim();
    }

    @Override
    public String getServiceType() {
        return "Commercial — " + commercialCategory;
    }

    @Override
    public double getExtensionRatePerHour() {
        return 500.0;
    }

    @Override
    public void displayServiceDetails() {
        System.out.println("  Package  : Commercial");
        System.out.println("  Category : " + commercialCategory);
        System.out.println("  Session  : 120 minutes | P2,000.00 | +P500/hr");
        System.out.println("  Delivery : Soft copies via Google Drive");
    }
}

// BookingEntry — implements Bookable
class BookingEntry implements Bookable {

    private PhotoshootService service; // POLYMORPHISM
    private int extraHours;

    public BookingEntry(PhotoshootService service, int extraHours) {
        this.service = service;
        this.extraHours = extraHours;
    }

    @Override
    public void book() {
        service.setStatus("Confirmed");
    }

    @Override
    public void cancel() {
        service.setStatus("Cancelled");
    }

    @Override
    public String getBookingStatus() {
        return service.getStatus();
    }

    public void showDetails() {
        service.displayServiceDetails();
    }

    public void showReceipt() {
        service.printReceipt(extraHours);
    }

    public String getReceiptText() {
        return service.getReceiptText(extraHours);
    }

    public PhotoshootService getService() {
        return service;
    }

    public int getExtraHours() {
        return extraHours;
    }
}

// MAIN GUI — GChromaLensSystem
public class GChromaLensSystem extends JFrame {

    // ── Palette
    static final Color DARK_SAGE = new Color(42, 66, 50); // darkest header green
    static final Color MED_SAGE = new Color(80, 110, 90); // medium sage
    static final Color SOFT_SAGE = new Color(160, 185, 165); // muted sage buttons
    static final Color PALE_SAGE = new Color(210, 225, 212); // very light sage bg
    static final Color LIGHTER_SAGE = new Color(230, 238, 232); // card background tint
    static final Color CREAM = new Color(245, 243, 238); // page background
    static final Color WHITE = Color.WHITE;
    static final Color DARK = new Color(25, 30, 25);
    static final Color MUTED = new Color(100, 115, 100);
    static final Color BORDER = new Color(190, 208, 193);
    static final Color RED_DEEP = new Color(160, 40, 30);
    static final Color RED_PALE = new Color(252, 235, 233);

    // fonts
    static Font FONT_TITLE;
    static Font FONT_HEADING;
    static Font FONT_BODY;
    static Font FONT_SMALL;
    static Font FONT_BOLD;
    static Font FONT_MONO = new Font("Monospaced", Font.PLAIN, 12);
    static Font FONT_BRAND;

    static {
        Font base = loadCormorant("CormorantSC-Regular.ttf");
        Font bold = loadCormorant("CormorantSC-Bold.ttf");

        FONT_BRAND = bold.deriveFont(Font.BOLD, 26f);
        FONT_TITLE = bold.deriveFont(Font.BOLD, 22f);
        FONT_HEADING = bold.deriveFont(Font.BOLD, 16f);
        FONT_BODY = base.deriveFont(Font.PLAIN, 14f);
        FONT_SMALL = base.deriveFont(Font.PLAIN, 12f);
        FONT_BOLD = bold.deriveFont(Font.BOLD, 14f);
    }

    static Font loadCormorant(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists())
                f = new File("fonts/" + filename);
            if (f.exists()) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                return font;
            }
        } catch (Exception ignored) {
        }

        Font byName = new Font("Cormorant SC", Font.PLAIN, 14);
        if (byName.getFamily().equals("Cormorant SC"))
            return byName;
        return null;
    }

    // State
    private final List<BookingEntry> bookings = new ArrayList<>();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // Form fields (shared across details screen)
    private int selectedPkg = 0;
    private JTextField tfClientName, tfBookingDate, tfExtraHours, tfSubjects;
    private JComboBox<String> cbSubtype;
    private JLabel lblSubtypeLabel, lblPkgTitle, lblPkgDesc;
    private JPanel pnlSubjectsRow, pnlSubtypeField;

    static final String LOGO_FILE = "logo.png";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new GChromaLensSystem().setVisible(true);
        });
    }

    public GChromaLensSystem() {
        setTitle("G ChromaLens — Photoshoot Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 720);
        setMinimumSize(new Dimension(400, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(CREAM);

        mainPanel.setBackground(CREAM);
        add(mainPanel);

        buildWelcomeScreen();
        buildMainMenuScreen();
        buildBookServiceScreen();
        buildDetailsScreen();
        buildViewBookingsScreen();
        buildViewReceiptScreen();
        buildCancelScreen();

        showScreen("welcome");
    }

    // ── Navigation
    void showScreen(String name) {
        cardLayout.show(mainPanel, name);
        if (name.equals("view-bookings"))
            refreshBookingsList();
        if (name.equals("view-receipt"))
            refreshReceiptList();
        if (name.equals("cancel"))
            refreshCancelList();
    }

    // REUSABLE UI HELPERS

    JPanel makeHeader(String screenTitle, String backScreen) {
        // Outer wrapper: header + breadcrumb stacked
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        // Top banner
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(DARK_SAGE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        banner.setOpaque(false);
        banner.setBorder(new EmptyBorder(0, 0, 0, 0));
        banner.setPreferredSize(new Dimension(0, 100));

        // PNG Logo
        ImageIcon originalIcon = new ImageIcon("logowhite.png");

        Image scaledImage = originalIcon.getImage().getScaledInstance(85, 85, Image.SCALE_SMOOTH);

        JLabel logoIcon = new JLabel(new ImageIcon(scaledImage));
        logoIcon.setOpaque(false);
        logoIcon.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(0, 0, 0, 14));

        if (backScreen != null) {
            JButton menuBtn = new JButton("≡ Menu");
            styleHeaderBtn(menuBtn);
            menuBtn.addActionListener(e -> showScreen("main-menu"));
            right.add(menuBtn, BorderLayout.CENTER);
        }

        banner.add(logoIcon, BorderLayout.WEST);
        banner.add(right, BorderLayout.EAST);

        right.setOpaque(false);
        right.setBorder(new EmptyBorder(0, 0, 0, 14));
        if (backScreen != null) {
            JButton menuBtn = new JButton("Menu");

            menuBtn.setFont(new Font("Cormorant SC", Font.PLAIN, 15));
            menuBtn.setForeground(Color.WHITE);

            menuBtn.setFocusPainted(false);
            menuBtn.setBorderPainted(false);
            menuBtn.setContentAreaFilled(false);
            menuBtn.setOpaque(false);

            menuBtn.setBorder(null);
            menuBtn.setMargin(new Insets(0, 0, 0, 0));

            menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            menuBtn.addActionListener(e -> showScreen("main-menu"));

            right.add(menuBtn, BorderLayout.CENTER);
        }

        banner.add(logoIcon, BorderLayout.WEST);
        banner.add(right, BorderLayout.EAST);

        // Breadcrumb
        JPanel bread = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 5));
        bread.setBackground(PALE_SAGE);
        bread.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        JLabel bLbl = new JLabel(screenTitle);
        bLbl.setFont(FONT_SMALL);
        bLbl.setForeground(MED_SAGE);
        bread.add(bLbl);

        wrapper.add(banner, BorderLayout.NORTH);
        wrapper.add(bread, BorderLayout.SOUTH);
        return wrapper;
    }

    void styleHeaderBtn(JButton b) {
        b.setFont(FONT_SMALL);
        b.setForeground(WHITE);
        b.setBackground(new Color(255, 255, 255, 0));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1, true), new EmptyBorder(4, 10, 4, 10)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Flat button */
    JButton flatBtn(String text, Color fg, Color bg) {
        JButton b = new JButton(text);
        b.setForeground(fg);
        b.setBackground(bg);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(FONT_BODY);
        return b;
    }

    /** Dark sage filled button */
    JButton primaryBtn(String text) {
        JButton b = flatBtn(text, WHITE, DARK_SAGE);
        b.setFont(FONT_BOLD);
        b.setBorder(new EmptyBorder(9, 22, 9, 22));
        b.addMouseListener(hoverEffect(b, DARK_SAGE, MED_SAGE));
        return b;
    }

    /** Muted sage outline button (style from mockup) */
    JButton softBtn(String text) {
        JButton b = flatBtn(text, DARK_SAGE, SOFT_SAGE);
        b.setFont(FONT_BOLD);
        b.setBorder(new EmptyBorder(9, 22, 9, 22));
        b.addMouseListener(hoverEffect(b, SOFT_SAGE, PALE_SAGE));
        return b;
    }

    /** Red danger button */
    JButton dangerBtn(String text) {
        JButton b = flatBtn(text, WHITE, RED_DEEP);
        b.setFont(FONT_BOLD);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        return b;
    }

    MouseAdapter hoverEffect(JButton b, Color normal, Color hover) {
        return new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(hover);
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(normal);
            }
        };
    }

    JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_BODY);
        tf.setForeground(DARK);
        tf.setBackground(WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(DARK_SAGE, 1, true),
                        new EmptyBorder(6, 10, 6, 10)));
            }

            public void focusLost(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1, true),
                        new EmptyBorder(6, 10, 6, 10)));
            }
        });
        return tf;
    }

    JPanel formRow(String labelText, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font(FONT_SMALL.getFamily(), Font.BOLD, 11));
        lbl.setForeground(MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        p.add(lbl);
        p.add(Box.createVerticalStrut(3));
        p.add(field);
        return p;
    }

    JPanel bottomBar(JComponent... btns) {
        JPanel bar = new JPanel(new GridLayout(1, btns.length, 10, 0));
        bar.setBackground(WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(12, 16, 12, 16)));
        for (JComponent b : btns)
            bar.add(b);
        return bar;
    }

    JLabel statusChip(String status) {
        JLabel l = new JLabel("  " + status + "  ");
        l.setFont(FONT_SMALL);
        l.setOpaque(true);
        if ("Confirmed".equals(status)) {
            l.setBackground(PALE_SAGE);
            l.setForeground(DARK_SAGE);
        } else {
            l.setBackground(RED_PALE);
            l.setForeground(RED_DEEP);
        }
        l.setBorder(new EmptyBorder(2, 6, 2, 6));
        return l;
    }

    JPanel infoRow(String label, String value) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setBorder(new EmptyBorder(2, 0, 2, 0));
        JLabel l = new JLabel(label);
        l.setFont(FONT_SMALL);
        l.setForeground(MUTED);
        JLabel v = new JLabel(value);
        v.setFont(FONT_SMALL);
        v.setForeground(DARK);
        r.add(l, BorderLayout.WEST);
        r.add(v, BorderLayout.EAST);
        return r;
    }

    void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    void buildWelcomeScreen() {
        BufferedImage bgImg = null;

        try {
            bgImg = ImageIO.read(new File("backgroundmenu.png"));
        } catch (Exception ignored) {
        }

        final BufferedImage finalBg = bgImg;

        JPanel screen = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (finalBg != null) {
                    Graphics2D g2 = (Graphics2D) g.create();

                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    // draw full screen background
                    g2.drawImage(finalBg, 0, 0, getWidth(), getHeight(), null);

                    // optional dark overlay for aesthetic
                    g2.setColor(new Color(255, 255, 255, 80));
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    g2.dispose();
                }
            }
        };

        // ── Centre column
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        col.setBorder(new EmptyBorder(50, 40, 0, 40));

        // "WELCOME TO" heading
        JLabel welcomeLbl = new JLabel("WELCOME TO", SwingConstants.CENTER);
        welcomeLbl.setFont(FONT_HEADING.deriveFont(Font.BOLD, 25f));
        welcomeLbl.setForeground(DARK_SAGE);
        welcomeLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        BufferedImage rawImg = null;
        try {
            File f = new File(LOGO_FILE);
            if (!f.exists())
                f = new File("images/" + LOGO_FILE);
            if (f.exists())
                rawImg = ImageIO.read(f);
        } catch (Exception ignored) {
        }
        final BufferedImage logoImg = rawImg;

        JPanel logoArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                if (logoImg != null) {
                    // Draw logo scaled to fit, preserving aspect ratio
                    int pw = getWidth(), ph = getHeight();
                    int iw = logoImg.getWidth(), ih = logoImg.getHeight();
                    double scale = Math.min((double) pw / iw, (double) ph / ih);
                    int dw = (int) (iw * scale), dh = (int) (ih * scale);
                    int dx = (pw - dw) / 2, dy = (ph - dh) / 2;
                    g2.drawImage(logoImg, dx, dy, dw, dh, null);
                } else {
                    // Placeholder — drawn camera outline (dark sage on white)
                    g2.setColor(new Color(220, 230, 222));
                    g2.setStroke(new BasicStroke(2f));
                    int pw = getWidth(), ph = getHeight();
                    int s = Math.min(pw, ph) - 20;
                    int x = (pw - s) / 2, y = (ph - s) / 2;
                    g2.drawOval(x, y, s, s);
                    g2.setColor(DARK_SAGE);
                    g2.setStroke(new BasicStroke(2.5f));
                    int cx = pw / 2, cy = ph / 2;
                    g2.drawOval(cx - s / 5, cy - s / 5, s * 2 / 5, s * 2 / 5);
                    // camera body shape
                    int bx = x + s / 6, by = cy - s / 6, bw = s * 2 / 3, bh = s / 3;
                    g2.drawRoundRect(bx, by, bw, bh, 10, 10);
                    g2.drawRect(cx - 8, by - 8, 16, 10);
                    // G initial
                    g2.setFont(new Font(FONT_BRAND.getFamily(), Font.BOLD, s / 3));
                    g2.setColor(DARK_SAGE);
                    FontMetrics fm = g2.getFontMetrics();
                    String letter = "G";
                    g2.drawString(letter, cx - fm.stringWidth(letter) / 2, cy + fm.getAscent() / 3);
                }
                g2.dispose();
            }
        };
        logoArea.setOpaque(false);
        logoArea.setPreferredSize(new Dimension(300, 300));
        logoArea.setMaximumSize(new Dimension(300, 300));
        logoArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "Book a Session" — dark sage rounded button
        JButton btnSession = new JButton("Book a Session") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                /* no border */ }
        };
        btnSession.setFont(FONT_BOLD);
        btnSession.setForeground(WHITE);
        btnSession.setBackground(DARK_SAGE);
        btnSession.setOpaque(false);
        btnSession.setContentAreaFilled(false);
        btnSession.setFocusPainted(false);
        btnSession.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSession.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSession.setMaximumSize(new Dimension(220, 44));
        btnSession.setPreferredSize(new Dimension(220, 44));
        btnSession.setBorder(new EmptyBorder(10, 30, 10, 30));
        btnSession.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnSession.setBackground(MED_SAGE);
            }

            public void mouseExited(MouseEvent e) {
                btnSession.setBackground(DARK_SAGE);
            }
        });
        btnSession.addActionListener(e -> showScreen("main-menu"));

        col.add(Box.createVerticalGlue());
        col.add(welcomeLbl);
        col.add(Box.createVerticalStrut(18));
        col.add(logoArea);
        col.add(Box.createVerticalStrut(14));
        col.add(Box.createVerticalStrut(36));
        col.add(btnSession);
        col.add(Box.createVerticalGlue());

        // Contact at bottom
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(0, 0, 18, 0));
        JLabel contact = new JLabel("+63 906 586 4651");
        contact.setFont(FONT_SMALL);
        contact.setForeground(new Color(6, 64, 43));
        bottom.add(contact);

        screen.add(col, BorderLayout.CENTER);
        screen.add(bottom, BorderLayout.SOUTH);
        mainPanel.add(screen, "welcome");
    }

    /** Rounded soft-sage outlined menu button (Main Menu style) */
    JButton makeMenuBtn(String text, String screen) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PALE_SAGE);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };
        b.setFont(FONT_BOLD);
        b.setForeground(DARK_SAGE);
        b.setBackground(LIGHTER_SAGE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(280, 42));
        b.setPreferredSize(new Dimension(280, 42));
        b.setBorder(new EmptyBorder(8, 20, 8, 20));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(PALE_SAGE);
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(LIGHTER_SAGE);
            }
        });
        b.addActionListener(e -> showScreen(screen));
        return b;
    }

    void styleSmallDarkBtn(JButton b) {
        b.setFont(FONT_BODY);
        b.setForeground(WHITE);
        b.setBackground(DARK_SAGE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(120, 36));
        b.setPreferredSize(new Dimension(120, 36));
        b.setBorder(new EmptyBorder(7, 18, 7, 18));
    }

    // ═════════════════════════════════════════════════════════
    // SCREEN 2 — MAIN MENU
    // ═════════════════════════════════════════════════════════
    void buildMainMenuScreen() {

        BufferedImage bgImg = null;
        BufferedImage logoImg = null;

        try {
            bgImg = ImageIO.read(new File("backgroundmenu.png"));
            logoImg = ImageIO.read(new File("logo.png")); // transparent logo
        } catch (Exception ignored) {
        }

        final BufferedImage finalBg = bgImg;
        final BufferedImage finalLogo = logoImg;

        JPanel screen = new JPanel(new BorderLayout()) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                // FULL BACKGROUND PNG
                if (finalBg != null) {
                    g2.drawImage(finalBg, 0, 0, getWidth(), getHeight(), null);
                }

                // OPTIONAL DARK OVERLAY FOR LUXURY LOOK
                g2.setColor(new Color(0, 0, 0, 70));
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        };

        screen.setLayout(new BorderLayout());

        // ─────────────────────────────────────────
        // CENTER CONTENT
        // ─────────────────────────────────────────
        JPanel contentWrapper = new JPanel();
        contentWrapper.setOpaque(false);
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBorder(new EmptyBorder(20, 30, 20, 30));

        // LOGO
        JLabel logoLabel = new JLabel();

        if (finalLogo != null) {

            Image scaledLogo = finalLogo.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

            logoLabel.setIcon(new ImageIcon(scaledLogo));
        }

        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // BRAND NAME
        JLabel brandLbl = new JLabel("G ChromaLens");
        brandLbl.setFont(FONT_BRAND.deriveFont(25f));
        brandLbl.setForeground(Color.WHITE);
        brandLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("CAPTURING FACES, FRAMING EMOTIONS");
        subtitle.setFont(FONT_HEADING.deriveFont(10f));
        subtitle.setForeground(new Color(255, 255, 255, 180));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // BUTTON CONTAINER
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        buttonPanel.add(makeMenuBtn("Book a Service", "book-service"));
        buttonPanel.add(Box.createVerticalStrut(18));

        buttonPanel.add(makeMenuBtn("View Availability", "view-bookings"));
        buttonPanel.add(Box.createVerticalStrut(18));

        buttonPanel.add(makeMenuBtn("View Receipt", "view-receipt"));
        buttonPanel.add(Box.createVerticalStrut(18));

        buttonPanel.add(makeMenuBtn("Cancel a Booking", "cancel"));
        buttonPanel.add(Box.createVerticalStrut(28));

        JButton exitBtn = new JButton("Exit");
        styleSmallDarkBtn(exitBtn);

        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        exitBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Thank you for choosing G ChromaLens!\nExit?", "Exit",
                    JOptionPane.YES_NO_OPTION);

            if (c == JOptionPane.YES_OPTION)
                System.exit(0);
        });

        buttonPanel.add(exitBtn);

        // ADD COMPONENTS
        contentWrapper.add(logoLabel);
        contentWrapper.add(Box.createVerticalStrut(10));
        contentWrapper.add(brandLbl);
        contentWrapper.add(Box.createVerticalStrut(5));
        contentWrapper.add(subtitle);
        contentWrapper.add(Box.createVerticalStrut(20));
        contentWrapper.add(buttonPanel);

        // SCROLL
        JScrollPane scroll = new JScrollPane(contentWrapper);

        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        screen.add(scroll, BorderLayout.CENTER);

        mainPanel.add(screen, "main-menu");
    }

    // SCREEN 3 — BOOK SERVICE (Package Selection)

    void buildBookServiceScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(CREAM);
        screen.add(makeHeader("Main Menu > Package Selection", "main-menu"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(CREAM);
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Select a Package");
        title.setFont(FONT_HEADING);
        title.setForeground(DARK_SAGE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(14));

        // Package cards
        Object[][] pkgs = {
                { 1, "Individual Portrait", "60 mins  ·  +P300/hr  ·  Newborn, Maternity, Funshoot", "P 500" },
                { 2, "Portrait", "120 mins ·  +P500/hr  ·  Pre-debut, Prenup, Couple, Family, Group", "P 1,000" },
                { 3, "Commercial", "120 mins ·  +P500/hr  ·  Product, Food, Fashion", "P 2,000" } };
        for (Object[] p : pkgs) {
            JPanel card = makePkgCard((int) p[0], (String) p[1], (String) p[2], (String) p[3]);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(card);
            content.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CREAM);

        JButton back = softBtn("← Back");
        back.addActionListener(e -> showScreen("main-menu"));
        JButton next = primaryBtn("Next →");
        next.addActionListener(e -> showScreen("book-service")); // placeholder; real action on Select

        screen.add(scroll, BorderLayout.CENTER);
        screen.add(bottomBar(back, next), BorderLayout.SOUTH);
        mainPanel.add(screen, "book-service");
    }

    JPanel makePkgCard(int pkgNum, String name, String desc, String price) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(14, 16, 14, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        JLabel nl = new JLabel(name);
        nl.setFont(FONT_BOLD);
        nl.setForeground(DARK);
        JLabel dl = new JLabel(desc);
        dl.setFont(FONT_SMALL);
        dl.setForeground(MUTED);
        JLabel pl = new JLabel(price);
        pl.setFont(new Font(FONT_HEADING.getFamily(), Font.BOLD, 16));
        pl.setForeground(DARK_SAGE);
        info.add(nl);
        info.add(Box.createVerticalStrut(2));
        info.add(dl);
        info.add(Box.createVerticalStrut(4));
        info.add(pl);

        JButton sel = primaryBtn("Select");
        sel.setFont(FONT_SMALL);
        sel.setBorder(new EmptyBorder(7, 14, 7, 14));
        sel.addActionListener(e -> openDetailsFor(pkgNum));

        card.add(info, BorderLayout.CENTER);
        card.add(sel, BorderLayout.EAST);
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openDetailsFor(pkgNum);
            }

            public void mouseEntered(MouseEvent e) {
                card.setBackground(LIGHTER_SAGE);
                info.setBackground(LIGHTER_SAGE);
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(WHITE);
                info.setBackground(WHITE);
            }
        });
        return card;
    }

    // SCREEN 4 — DETAILS CONFIRMATION
    void buildDetailsScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(CREAM);
        screen.add(makeHeader("Package Selection > Details Confirmation", "book-service"), BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Client's Information header
        JLabel ciHeader = new JLabel("Client's Information :");
        ciHeader.setFont(FONT_HEADING);
        ciHeader.setForeground(DARK_SAGE);
        ciHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfClientName = styledField();
        tfBookingDate = styledField();
        tfExtraHours = styledField();
        tfExtraHours.setText("0");
        tfSubjects = styledField();
        tfSubjects.setText("1");

        cbSubtype = new JComboBox<>();
        cbSubtype.setFont(FONT_BODY);
        cbSubtype.setBackground(WHITE);
        cbSubtype.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        lblSubtypeLabel = new JLabel("SUBTYPE");
        lblSubtypeLabel.setFont(new Font(FONT_SMALL.getFamily(), Font.BOLD, 11));
        lblSubtypeLabel.setForeground(MUTED);

        lblPkgTitle = new JLabel("");
        lblPkgTitle.setFont(FONT_BOLD);
        lblPkgTitle.setForeground(DARK_SAGE);

        lblPkgDesc = new JLabel("");
        lblPkgDesc.setFont(FONT_SMALL);
        lblPkgDesc.setForeground(MUTED);

        // Pkg summary mini card
        JPanel pkgSummary = new JPanel();
        pkgSummary.setLayout(new BoxLayout(pkgSummary, BoxLayout.Y_AXIS));
        pkgSummary.setBackground(LIGHTER_SAGE);
        pkgSummary.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        pkgSummary.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        pkgSummary.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPkgTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPkgDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        pkgSummary.add(lblPkgTitle);
        pkgSummary.add(lblPkgDesc);

        // Subtype panel
        pnlSubtypeField = new JPanel();
        pnlSubtypeField.setLayout(new BoxLayout(pnlSubtypeField, BoxLayout.Y_AXIS));
        pnlSubtypeField.setOpaque(false);
        pnlSubtypeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbSubtype.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSubtypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlSubtypeField.add(lblSubtypeLabel);
        pnlSubtypeField.add(Box.createVerticalStrut(3));
        pnlSubtypeField.add(cbSubtype);

        JPanel nameRow = formRow("Client's Name:", tfClientName);
        JPanel dateRow = formRow("Booking Date:", tfBookingDate);
        JPanel extRow = formRow("(Optional :) Extra Hours:", tfExtraHours);
        pnlSubjectsRow = formRow("No. of Subjects:", tfSubjects);
        nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        extRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlSubjectsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlSubtypeField.setBorder(new EmptyBorder(0, 0, 10, 0));

        form.add(pkgSummary);
        form.add(Box.createVerticalStrut(14));
        form.add(ciHeader);
        form.add(Box.createVerticalStrut(12));
        form.add(nameRow);
        form.add(dateRow);
        form.add(extRow);
        form.add(Box.createVerticalStrut(4));
        form.add(pnlSubtypeField);
        form.add(pnlSubjectsRow);
        form.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WHITE);

        JButton back = softBtn("← Back");
        back.addActionListener(e -> showScreen("book-service"));
        JButton confirm = primaryBtn("Next →");
        confirm.addActionListener(e -> doConfirmBooking());

        screen.add(scroll, BorderLayout.CENTER);
        screen.add(bottomBar(back, confirm), BorderLayout.SOUTH);
        mainPanel.add(screen, "details");
    }

    void openDetailsFor(int pkg) {
        selectedPkg = pkg;
        String[][] info = { null,
                { "Individual Portrait", "P500 · 60 mins · +P300/hr", "Subtypes :", "Newborn", "Maternity",
                        "Funshoot" },
                { "Portrait", "P1,000 · 120 mins · +P500/hr", "Occasion :", "Pre-debut", "Prenup", "Couple", "Family",
                        "Group" },
                { "Commercial", "P2,000 · 120 mins · +P500/hr", "Category :", "Product", "Food", "Fashion" } };
        lblPkgTitle.setText(info[pkg][0]);
        lblPkgDesc.setText(info[pkg][1]);
        lblSubtypeLabel.setText(info[pkg][2].toUpperCase());
        cbSubtype.removeAllItems();
        for (int i = 3; i < info[pkg].length; i++)
            cbSubtype.addItem(info[pkg][i]);
        pnlSubjectsRow.setVisible(pkg == 2);
        tfClientName.setText("");
        tfBookingDate.setText("");
        tfExtraHours.setText("0");
        showScreen("details");
    }

    void doConfirmBooking() {
        String name = tfClientName.getText().trim();
        String date = tfBookingDate.getText().trim();
        String sub = cbSubtype.getSelectedItem() != null ? cbSubtype.getSelectedItem().toString() : "";
        int extra = 0;
        try {
            extra = Integer.parseInt(tfExtraHours.getText().trim());
        } catch (Exception ignored) {
        }

        if (name.isEmpty()) {
            showError("Client name cannot be empty.");
            return;
        }
        if (date.isEmpty()) {
            showError("Booking date cannot be empty.");
            return;
        }
        if (sub.isEmpty()) {
            showError("Please select a subtype.");
            return;
        }

        try {
            PhotoshootService svc;
            if (selectedPkg == 1) {
                svc = new IndividualPortrait(name, date, sub);
            } else if (selectedPkg == 2) {
                int subj = 1;
                try {
                    subj = Integer.parseInt(tfSubjects.getText().trim());
                } catch (Exception ignored) {
                }
                svc = new Portrait(name, date, sub, subj);
            } else {
                svc = new Commercial(name, date, sub);
            }
            BookingEntry entry = new BookingEntry(svc, extra);
            entry.book();
            bookings.add(entry);

            JTextArea ta = new JTextArea(entry.getReceiptText());
            ta.setFont(FONT_MONO);
            ta.setEditable(false);
            ta.setBackground(LIGHTER_SAGE);
            ta.setBorder(new EmptyBorder(12, 12, 12, 12));
            JScrollPane sp = new JScrollPane(ta);
            sp.setPreferredSize(new Dimension(400, 280));
            JOptionPane.showMessageDialog(this, sp, "Booking Confirmed ✓", JOptionPane.INFORMATION_MESSAGE);
            showScreen("view-bookings");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    // SCREEN 5 — VIEW ALL BOOKINGS
    JPanel bookingsListPanel;

    void buildViewBookingsScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(CREAM);
        screen.add(makeHeader("Main Menu > View All Bookings", "main-menu"), BorderLayout.NORTH);

        bookingsListPanel = new JPanel();
        bookingsListPanel.setLayout(new BoxLayout(bookingsListPanel, BoxLayout.Y_AXIS));
        bookingsListPanel.setBackground(CREAM);
        bookingsListPanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JScrollPane scroll = new JScrollPane(bookingsListPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CREAM);

        screen.add(scroll, BorderLayout.CENTER);
        mainPanel.add(screen, "view-bookings");
    }

    void refreshBookingsList() {
        bookingsListPanel.removeAll();
        if (bookings.isEmpty()) {
            JLabel e = new JLabel("No bookings yet. Book your first session!", SwingConstants.CENTER);
            e.setFont(FONT_BODY);
            e.setForeground(MUTED);
            e.setAlignmentX(Component.CENTER_ALIGNMENT);
            bookingsListPanel.add(Box.createVerticalStrut(60));
            bookingsListPanel.add(e);
        } else {
            for (int i = 0; i < bookings.size(); i++) {
                bookingsListPanel.add(makeBookingCard(i));
                bookingsListPanel.add(Box.createVerticalStrut(10));
            }
        }
        bookingsListPanel.revalidate();
        bookingsListPanel.repaint();
    }

    JPanel makeBookingCard(int i) {
        BookingEntry entry = bookings.get(i);
        PhotoshootService s = entry.getService();
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        JLabel nm = new JLabel("✓  " + s.getClientName());
        nm.setFont(FONT_BOLD);
        nm.setForeground(DARK);
        JLabel bg = new JLabel(i == bookings.size() - 1 ? "  NEW  " : "  #" + (i + 1) + "  ");
        bg.setFont(FONT_SMALL);
        bg.setOpaque(true);
        bg.setBackground(PALE_SAGE);
        bg.setForeground(DARK_SAGE);
        hdr.add(nm, BorderLayout.WEST);
        hdr.add(bg, BorderLayout.EAST);
        card.add(hdr);
        card.add(Box.createVerticalStrut(6));
        card.add(infoRow("Service", s.getServiceType()));
        card.add(infoRow("Date", s.getBookingDate()));
        card.add(infoRow("Total", String.format("P%.2f", s.calculateTotal(entry.getExtraHours()))));
        JPanel sr = new JPanel(new BorderLayout());
        sr.setOpaque(false);
        JLabel sl = new JLabel("Status");
        sl.setFont(FONT_SMALL);
        sl.setForeground(MUTED);
        sr.add(sl, BorderLayout.WEST);
        sr.add(statusChip(entry.getBookingStatus()), BorderLayout.EAST);
        card.add(sr);
        return card;
    }

    // SCREEN 6 — VIEW RECEIPT
    JPanel receiptListPanel;

    void buildViewReceiptScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(CREAM);
        screen.add(makeHeader("Main Menu > Print Receipt", "main-menu"), BorderLayout.NORTH);

        receiptListPanel = new JPanel();
        receiptListPanel.setLayout(new BoxLayout(receiptListPanel, BoxLayout.Y_AXIS));
        receiptListPanel.setBackground(CREAM);
        receiptListPanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JScrollPane scroll = new JScrollPane(receiptListPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CREAM);

        screen.add(scroll, BorderLayout.CENTER);
        mainPanel.add(screen, "view-receipt");
    }

    void refreshReceiptList() {
        receiptListPanel.removeAll();
        if (bookings.isEmpty()) {
            JLabel e = new JLabel("No bookings available.", SwingConstants.CENTER);
            e.setFont(FONT_BODY);
            e.setForeground(MUTED);
            e.setAlignmentX(Component.CENTER_ALIGNMENT);
            receiptListPanel.add(Box.createVerticalStrut(60));
            receiptListPanel.add(e);
        } else {
            JLabel hint = new JLabel("Select booking number:");
            hint.setFont(FONT_SMALL);
            hint.setForeground(MUTED);
            hint.setAlignmentX(Component.LEFT_ALIGNMENT);
            receiptListPanel.add(hint);
            receiptListPanel.add(Box.createVerticalStrut(10));
            for (int i = 0; i < bookings.size(); i++) {
                final int idx = i;
                BookingEntry entry = bookings.get(i);
                PhotoshootService s = entry.getService();

                JPanel row = new JPanel(new BorderLayout(10, 0));
                row.setBackground(WHITE);
                row.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1, true),
                        new EmptyBorder(10, 14, 10, 14)));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
                row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                info.setOpaque(false);
                JLabel nl = new JLabel("#" + (i + 1) + "  " + s.getClientName());
                nl.setFont(FONT_BOLD);
                nl.setForeground(DARK);
                JLabel sl = new JLabel(s.getServiceType());
                sl.setFont(FONT_SMALL);
                sl.setForeground(MUTED);
                info.add(nl);
                info.add(sl);

                JButton vb = primaryBtn("View →");
                vb.setFont(FONT_SMALL);
                vb.setBorder(new EmptyBorder(6, 12, 6, 12));
                vb.addActionListener(ev -> showReceiptDialog(idx));

                row.add(info, BorderLayout.CENTER);
                row.add(vb, BorderLayout.EAST);
                row.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent ev) {
                        showReceiptDialog(idx);
                    }

                    public void mouseEntered(MouseEvent ev) {
                        row.setBackground(LIGHTER_SAGE);
                        info.setBackground(LIGHTER_SAGE);
                    }

                    public void mouseExited(MouseEvent ev) {
                        row.setBackground(WHITE);
                        info.setBackground(WHITE);
                    }
                });
                receiptListPanel.add(row);
                receiptListPanel.add(Box.createVerticalStrut(8));
            }
        }
        receiptListPanel.revalidate();
        receiptListPanel.repaint();
    }

    void showReceiptDialog(int idx) {
        BookingEntry entry = bookings.get(idx);
        JTextArea ta = new JTextArea(entry.getReceiptText());
        ta.setFont(FONT_MONO);
        ta.setEditable(false);
        ta.setBackground(LIGHTER_SAGE);
        ta.setBorder(new EmptyBorder(12, 12, 12, 12));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(420, 300));
        Object[] opts = { "Print (Console)", "Close" };
        int c = JOptionPane.showOptionDialog(this, sp, "Receipt — " + entry.getService().getClientName(),
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[1]);
        if (c == 0) {
            entry.showReceipt();
            JOptionPane.showMessageDialog(this, "Printed to console.");
        }
    }

    // SCREEN 7 — CANCEL / CONFIRM BOOKING
    JPanel cancelListPanel;

    void buildCancelScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(CREAM);
        screen.add(makeHeader("Main Menu > Cancel / Confirm Booking", "main-menu"), BorderLayout.NORTH);

        cancelListPanel = new JPanel();
        cancelListPanel.setLayout(new BoxLayout(cancelListPanel, BoxLayout.Y_AXIS));
        cancelListPanel.setBackground(CREAM);
        cancelListPanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JScrollPane scroll = new JScrollPane(cancelListPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CREAM);

        screen.add(scroll, BorderLayout.CENTER);
        mainPanel.add(screen, "cancel");
    }

    void refreshCancelList() {
        cancelListPanel.removeAll();
        if (bookings.isEmpty()) {
            JLabel e = new JLabel("No bookings to manage.", SwingConstants.CENTER);
            e.setFont(FONT_BODY);
            e.setForeground(MUTED);
            e.setAlignmentX(Component.CENTER_ALIGNMENT);
            cancelListPanel.add(Box.createVerticalStrut(60));
            cancelListPanel.add(e);
        } else {
            JLabel hint = new JLabel("Select a booking to manage:");
            hint.setFont(FONT_SMALL);
            hint.setForeground(MUTED);
            hint.setAlignmentX(Component.LEFT_ALIGNMENT);
            cancelListPanel.add(hint);
            cancelListPanel.add(Box.createVerticalStrut(10));
            for (int i = 0; i < bookings.size(); i++) {
                cancelListPanel.add(makeCancelCard(i));
                cancelListPanel.add(Box.createVerticalStrut(10));
            }
        }
        cancelListPanel.revalidate();
        cancelListPanel.repaint();
    }

    JPanel makeCancelCard(int i) {
        BookingEntry entry = bookings.get(i);
        PhotoshootService s = entry.getService();

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        JLabel nm = new JLabel("✓  " + s.getClientName());
        nm.setFont(FONT_BOLD);
        nm.setForeground(DARK);
        hdr.add(nm, BorderLayout.WEST);
        hdr.add(statusChip(entry.getBookingStatus()), BorderLayout.EAST);
        card.add(hdr);
        card.add(Box.createVerticalStrut(6));
        card.add(infoRow("Service", s.getServiceType()));
        card.add(infoRow("Total", String.format("P%.2f", s.calculateTotal(entry.getExtraHours()))));
        card.add(Box.createVerticalStrut(10));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setOpaque(false);

        JButton btnCancel = dangerBtn("Cancel Booking");
        btnCancel.setEnabled(!"Cancelled".equals(entry.getBookingStatus()));
        btnCancel.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this,
                    "Cancel booking for " + s.getClientName() + "?\nService: " + s.getServiceType(), "Cancel Booking?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (c == JOptionPane.YES_OPTION) {
                entry.cancel();
                refreshCancelList();
            }
        });

        JButton btnConfirm = primaryBtn("Confirm Booking");
        btnConfirm.setEnabled(!"Confirmed".equals(entry.getBookingStatus()));
        btnConfirm.addActionListener(e -> {
            entry.book();
            refreshCancelList();
        });

        btnRow.add(btnCancel);
        btnRow.add(btnConfirm);
        card.add(btnRow);
        return card;
    }
}
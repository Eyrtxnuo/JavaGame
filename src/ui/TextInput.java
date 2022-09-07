package ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import main.Game;
import utils.AudioPlayer;
import static utils.Constants.UI.TextInput.TI_HEIGHT;
import static utils.Constants.UI.TextInput.TI_HEIGHT_DEFAULT;
import static utils.Constants.UI.TextInput.TI_WIDTH;
import static utils.Constants.UI.TextInput.TI_WIDTH_DEFAULT;
import utils.LoadSave;
import static utils.Utils.isPrintableChar;

/**
 *
 * @author matti
 */
public class TextInput implements onClick {//TODO: selections : ctrl+c 

    private int xPos, yPos, index;

    private int yOffestCenter = TI_HEIGHT / 2;

    private boolean cursor = false;
    private boolean insert = false;

    private int cursorIndex = 0;

    private String text = "localhost";
    private int leftSkipped = 0;
    private int rightSkipped = 0;

    private static final String PREFIX = "…";
    private static final String SUFFIX = "…";

    private Font font = LoadSave.FONT.deriveFont(25 * Game.SCALE);

    private int animTick;
    private final int animSpeed = 100;

    private BufferedImage[] imgs;

    private Boolean mouseOver = false, selected = false;

    private Rectangle bounds;
    private final int leftBorder = (int) (new Canvas().getFontMetrics(font).stringWidth(PREFIX) + 4 * Game.SCALE);
    private final int rightBorder = (int) (new Canvas().getFontMetrics(font).stringWidth(SUFFIX) + 4 * Game.SCALE);
    private final int upperBorder = (int) (9 * Game.SCALE);

    public TextInput(int xPos, int yPos) {

        this.xPos = xPos;
        this.yPos = yPos;
        loadImgs();
        initBounds();
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_TEXTINPUT);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * TI_WIDTH_DEFAULT, 0, TI_WIDTH_DEFAULT, TI_HEIGHT_DEFAULT);
        }
    }

    private void initBounds() {
        bounds = new Rectangle(xPos, yPos - yOffestCenter, TI_WIDTH, TI_HEIGHT);
    }

    public void draw(Graphics g) {

        g.drawImage(imgs[index], xPos, yPos - yOffestCenter, bounds.width, bounds.height, null);
        Color old = g.getColor();
        g.setColor(Color.white);

        g.setFont(font);
        String printText = text;

        //System.out.print("Cursor index: " + cursorIndex + ";  leftSkip:" + leftSkipped + ";  rightSkip:" + rightSkipped + ";   lenght:" + text.length());
        while (leftSkipped < printText.length() - rightSkipped
                && rightSkipped < printText.length() - 1
                && rightSkipped > 0
                && g.getFontMetrics().stringWidth(printText.substring(leftSkipped, printText.length() - rightSkipped)) < (bounds.width - (leftBorder + rightBorder))) {
            rightSkipped--;
        }
        if (!(g.getFontMetrics().stringWidth(printText.substring(leftSkipped, printText.length() - rightSkipped)) < (bounds.width - (leftBorder + rightBorder)))) {
            rightSkipped++;
        }

        //System.out.println("\n"+rightSkipped);
        if (leftSkipped == printText.length()
                || cursorIndex == printText.length()
                || cursorIndex > printText.substring(leftSkipped).length()
                || cursorIndex < rightSkipped
                || g.getFontMetrics().stringWidth(printText.substring(leftSkipped, printText.length() - rightSkipped)) > (bounds.width - (leftBorder + rightBorder))) {
            rightSkipped = 0;
            leftSkipped = 0;
            while (g.getFontMetrics().stringWidth(printText.substring(leftSkipped)) > (bounds.width - (leftBorder + rightBorder))) {
                leftSkipped++;
            }
            rightSkipped = 0;
            while (cursorIndex > printText.substring(leftSkipped).length()) {
                rightSkipped++;
                while (leftSkipped >= 0 && g.getFontMetrics().stringWidth(printText.substring(leftSkipped, printText.length() - rightSkipped)) < (bounds.width - (leftBorder + rightBorder))) {
                    leftSkipped--;
                }
                leftSkipped++;
            }
        }
        printText = printText.substring(leftSkipped, printText.length() - rightSkipped);
        //System.out.println(";   printLenght:" + printText.length());

        if (leftSkipped > 0) {
            g.drawString(PREFIX, (int) (xPos + 4 * Game.SCALE), yPos + upperBorder);
        }
        if (rightSkipped > 0) {
            g.drawString(SUFFIX, xPos + bounds.width - rightBorder, yPos + upperBorder);
        }
        g.drawString(printText, (int) (xPos + leftBorder), (int) (yPos + upperBorder));

        if (cursor) {
            if (cursorIndex == 0) {
                int x = (int) (xPos + leftBorder + g.getFontMetrics().stringWidth(printText));
                int y = (int) (yPos - yOffestCenter + g.getFontMetrics().getHeight() - g.getFontMetrics().getDescent());
                int width = (int) Math.min(
                        (g.getFontMetrics().charWidth('0')),
                        Math.max((xPos + bounds.width - rightBorder) - x, 0)
                );
                int height = (int) (2 * Game.SCALE);
                g.fillRect(x, y, width, height);
            } else {
                int x = (int) (xPos + leftBorder - 2 * Game.SCALE + g.getFontMetrics().stringWidth(text.substring(leftSkipped, text.length() - (cursorIndex))));
                int y = yPos - yOffestCenter + upperBorder;//g.getFontMetrics().getDescent();
                int width = insert ? g.getFontMetrics().charWidth(text.charAt(text.length() - cursorIndex)) : (int) (2 * Game.SCALE);
                int height = g.getFontMetrics().getHeight() - g.getFontMetrics().getAscent() / 2;
                g.fillRect(x, y, width, height);
                if (insert) {
                    x += 2 * Game.SCALE;
                    Color oldC = g.getColor();
                    g.setColor(new Color(255 - oldC.getRed(),
                            255 - oldC.getGreen(),
                            255 - oldC.getBlue()
                    ));
                    g.drawString(text.charAt(text.length() - cursorIndex) + "", x, (int) (yPos + upperBorder));
                    g.setColor(oldC);
                }
            }
        }
        g.setColor(old);
    }

    public void update() {
        if (selected) {
            animTick++;
            if (animTick > animSpeed) {
                animTick %= animSpeed;
                cursor = !cursor;
            }
        }

        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (selected) {
            index = 2;
        }
    }

    public Boolean getMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(Boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
        cursor &= selected;
    }

    public void clickEvent(MouseEvent e) {
        if (!onClick(e)) {
            return;
        }
        cursorIndex = getCursorIndexAtposition(e.getX() - xPos);
        AudioPlayer.playEffect(AudioPlayer.Effects.CLICK);
    }

    @Override
    public boolean onClick(MouseEvent e) {
        return true;
    }

    public void resetBools() {
        mouseOver = false;
        selected = false;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void processKeyEvent(KeyEvent e) {
        if (!selected) {
            return;
        }
        if (isPrintableChar(e.getKeyChar())) {
            append(e.getKeyChar());
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE -> {
                    if (cursorIndex == text.length()) {
                        break;
                    }
                    int intsertionPoint = text.length() - cursorIndex;
                    text = text.substring(0, intsertionPoint - 1) + text.substring(intsertionPoint);
                }

                case KeyEvent.VK_DELETE -> {
                    if (cursorIndex == 0) {
                        break;
                    }
                    int intsertionPoint = text.length() - cursorIndex;
                    text = text.substring(0, intsertionPoint) + text.substring(intsertionPoint + 1);
                    cursorIndex--;
                }

                case KeyEvent.VK_V -> {
                    if (!e.isControlDown()) {
                        break;
                    }
                    {
                        try {
                            append((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                        } catch (UnsupportedFlavorException | IOException ex) {
                            System.out.println("Clipboard is not text!");
                        }
                    }
                }
                case KeyEvent.VK_LEFT,KeyEvent.VK_DOWN -> {
                    if (cursorIndex < text.length()) {
                        cursorIndex++;
                    }
                }
                case KeyEvent.VK_RIGHT,KeyEvent.VK_UP -> {
                    if (cursorIndex > 0) {
                        cursorIndex--;
                    }
                }
                case KeyEvent.VK_INSERT -> {
                    insert = !insert;
                }
            }

        }
    }

    public void append(String str) {
        for (Character ch : str.toCharArray()) {
            append(ch);
        }
    }

    public void append(Character ch) {
        if (isPrintableChar(ch)) {
            int insertionPoint = text.length() - cursorIndex;
            int reinsertionPoint = insertionPoint;
            if (insert && cursorIndex > 0) {
                cursorIndex--;
                reinsertionPoint += 1;
            }
            text = text.substring(0, insertionPoint) + ch + text.substring(reinsertionPoint);

        }
    }

    public String getText() {
        return text;
    }

    private int getCursorIndexAtposition(int xRelativePos) {
        float x = xRelativePos - leftBorder;
        FontMetrics Fm = new Canvas().getFontMetrics(font);
        String displayed = text.substring(leftSkipped, text.length() - rightSkipped);
        int attempt = displayed.length();
        while (attempt > 0 && Fm.stringWidth(displayed.substring(0, attempt)) > xRelativePos) {
            attempt--;
        }
        return (displayed.length() - attempt) + rightSkipped;
    }
}

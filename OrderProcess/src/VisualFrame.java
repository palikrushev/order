import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class VisualFrame {

  private JTextArea textArea;
  private JFrame frame;

  public void initFrame() {

    frame = new JFrame();
    JButton button = setupButton();
    textArea = new JTextArea();

    BorderLayout borderLayout = new BorderLayout();
    borderLayout.addLayoutComponent(button, BorderLayout.NORTH);
    borderLayout.addLayoutComponent(textArea, BorderLayout.CENTER);
    frame.setLayout(borderLayout);

    frame.add(button);
    frame.add(textArea);

    setFrameToMouse();
    frame.pack();

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private JButton setupButton() {
    JButton button = new JButton("Generate");
    button.setMinimumSize(new Dimension(100, 20));
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        buttonIsClicked();
        frame.pack();
      }
    });

    return button;
  }

  private void buttonIsClicked() {
    String text = new OrderExtractor().extract();
    textArea.setText(text);

    StringSelection stringSelection = new StringSelection(text);
    Toolkit.getDefaultToolkit().getSystemClipboard()
        .setContents(stringSelection, stringSelection);
  }

  private void setFrameToMouse() {
    frame.setLocation(MouseInfo.getPointerInfo().getLocation());
  }
}

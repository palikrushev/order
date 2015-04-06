import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class OrderExtractor {

  private StringBuilder valuableText;
  private HashMap<String, Integer> personInOrder;
  private TreeMap<Integer, String> orderNumberToOrderText;
  private int nextOrderID;
  private String text;

  public String extract() {

    if (initializeVariables() == true) {
      readStreamAndProcess();
      printAndAssignOrders();
    }

    return valuableText.toString();
  }

  private boolean initializeVariables() {
    valuableText = new StringBuilder();
    personInOrder = new HashMap<String, Integer>();
    orderNumberToOrderText = new TreeMap<Integer, String>();
    nextOrderID = 0;

    try {
      text = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
          .getData(DataFlavor.stringFlavor);
    } catch (Exception e) {
      valuableText.append(e.getMessage());
      return false;
    }
    return true;
  }

  private void readStreamAndProcess() {
    StringTokenizer st = new StringTokenizer(text, "\n");

    while (st.hasMoreTokens()) {
      processLine(st.nextToken());
    }
  }

  private void processLine(String line) {
    String[] tags = extractTags(line);
    if (tags.length > 0) {

      for (int i = 0; i < tags.length; i++) {
        Integer orderNumber = personInOrder.remove(tags[i]);
        personInOrder.put(tags[i], nextOrderID);

        if (orderNumber != null) {
          orderNumberToOrderText.remove(orderNumber);
        }
      }

      orderNumberToOrderText.put(nextOrderID,
          line.substring(line.indexOf('#')));
      nextOrderID++;
    }
  }

  private String[] extractTags(String line) {
    ArrayList<String> result = new ArrayList<String>();

    for (int i = 0; i < line.length(); i++) {
      if (line.charAt(i) == '#') {
        int begin = i;
        int end = i + 1;

        while (end < line.length()
            && Character.isLetter(line.charAt(end))
            && Character.isUpperCase(line.charAt(end))) {
          end++;
        }
        int numberOfLetters = end - begin - 1;
        // Only if the tag consists of 2 or 3 capital letters
        if (numberOfLetters == 2 || numberOfLetters == 3) {
          result.add(line.substring(begin, end));
        }
      }
    }
    String[] resultArray = new String[result.size()];
    resultArray = result.toArray(resultArray);

    return resultArray;
  }

  private void printAndAssignOrders() {
    assignOrders();
  }

  private void assignOrders() {

    for (Iterator<String> iterator = orderNumberToOrderText.values()
        .iterator(); iterator.hasNext();) {
      String line = (String) iterator.next();

      valuableText.append(line + "\n");
    }

    assignTotal();
  }

  private void assignTotal() {
    String totalOrdersAndParticipants = "orders: "
        + orderNumberToOrderText.size() + "  participants: "
        + personInOrder.size();

    valuableText.append(totalOrdersAndParticipants + "\n");
  }

}

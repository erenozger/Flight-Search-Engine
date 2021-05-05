import java.util.ArrayList;

public class Operations {

    public static void readCommands(ArrayList<String> commandList) {
        for (int i = 0; i < commandList.size(); i++) {
            String[] splitList = commandList.get(i).split("\t");
            switch (splitList[0]) {
                case "listAll":
                    System.out.println("listAll");
                    break;
                case "listProper":
                    System.out.println("listProper");
                    break;
                case "listCheapest":
                    System.out.println("listCheapest");
                    break;
                case "listQuickest":
                    System.out.println("listQuickest");
                    break;
                case "listCheaper":
                    System.out.println("listCheaper");
                    break;
                case "listQuicker":
                    System.out.println("listQuicker");
                    break;
                case "listExcluding":
                    System.out.println("listExcluding");
                    break;
                case "listOnlyFrom":
                    System.out.println("listOnlyFrom");
                    break;
                case "diameterOfGraph":
                    System.out.println("diameterOfGraph");
                    break;
                case "pageRankOfNodes":
                    System.out.println("pageRankOfNodes");
                    break;
                default:
                    System.out.println("Undefined Command!!");
                    break;
            }
        }
    }

}

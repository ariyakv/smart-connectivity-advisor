public class App {
    public static void main(String[] args) throws Exception {
        System.out.println(App.stringCount("Ariya"));
    }

    public static int stringCount(String name) {
        return name.length();
    }
}

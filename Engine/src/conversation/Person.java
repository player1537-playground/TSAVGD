class Person {
  String name;
  Model model;
  public Person(String name) {
    this.name = name;
  }
  void init() {
    System.out.println("Overwrite init()");
  }
  void conversation() {
    System.out.println("Overwrite conversation()");
  }
  void setModel(Model m) {
    this.model = m;
  }
}

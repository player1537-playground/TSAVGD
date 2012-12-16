class Response {
  String text;
  Action act;
  public Response(String text, Action act) {
    this.text = text;
    this.act = act;
  }
  String getText() {
    return this.text;
  }
  Action getAction() {
    return this.act;
  }
}

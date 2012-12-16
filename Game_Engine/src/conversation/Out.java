class Out { public static void main(String[] args) {Person m = new Person("Martha") {
	void init() {
		setModel(new Model("martha.obj"));
	}
	void conversation() {
		if (!((PropertyManager.getValue("burned-down-house") != null && !PropertyManager.getValue("burned-down-house").equals("")))) {
			Conversation.printConv("Do you love me?");
			Conversation.getResponse(new Response("No", new Action() {
				void doAction() {
					PropertyManager.setValue("burned-house-down");
					PropertyManager.setValue("destroyed-love-life");
				}
			}
			),new Response("Yes", new Action() {
				void doAction() {
					PropertyManager.setValue("dtf");
				}
			}
			));
		}
		else {
			Conversation.printConv("I dislike you, foul person");
		}
	}
}
;
 m.init(); m.conversation(); PropertyManager.printMap(); }}

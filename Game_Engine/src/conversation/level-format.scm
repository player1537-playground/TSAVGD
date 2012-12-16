;; This buffer is for notes you don't want to save, and for Lisp evaluation.
;; If you want to create a file, visit that file with C-x C-f,
;; then enter the text in that file's own buffer.

[Martha "martha.jpg"]
>martha.obj
{burned-house-down} I DON'T LIKE YOU
{else} rawr
[Billy "billy.jpg"]







File: milk-bar.level
(level "milk-bar.obj")
(boundaries "door" "outside.level")
(character "Martha"
	   (picture "martha.obj")
	   (if (condition 'burned-house-down)
	       (conversation "Do you love me?"
			     '("No" (begin 
				      (set 'burned-house-down)
				      (set 'destroyed-love-life)))
			     '("Yes" (set 'she-loves-me)))
	       (conversation "I dislike you, foul person")))


File: options.inf
burned-house-down:true
destroyed-love-life:false






picture:martha.obj
conversation:






;; Separate

new Person("Martha", 
	   new ModelName("martha.obj"),
	   { 
	   new Conversation(new OptionCheck(new Domain("Martha", "burned-house-down")), "I DON'T LIKE YOU"),
	   new Conversation(new OptionTrue(), "rawr")
	   });
	   
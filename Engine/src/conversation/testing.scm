(character "Martha"
	   (model "martha.obj")
	   (if (not (condition 'burned-down-house))
	       (conversation "Do you love me?"
			     ("No" (begin
				     (set 'burned-house-down)
				     (set 'destroyed-love-life)))
			     ("Yes" (set 'dtf)))
	       (conversation "I dislike you, foul person")))

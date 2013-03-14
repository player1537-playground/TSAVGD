
(defun Person (&key name pos model conversation posfun)
  (class-def :instantiate t
	     :super "Person"
	     :body
	     (fun-def :name "init"
		      :rettype "void"
		      :body (list-remove-nils
			     (when model
			       (statement (fun-call "this" "setModel" model)))
			     (when pos
			       (statement (fun-call "this" "setPosition" pos)))
			     (when posfun
			       (statement (fun-call "this" "setPositionFunction" posfun)))
			     (when conversation 
			       (statement (fun-call "this" "setConversation" conversation)))))))

(defun class-def (&key super name instantiate body)
  (if instantiate
      (list "(new " super "(){" body "})")
    (list "class " name "{"
	  body
	  "}")))

(defun Level (&key name entities collide terrain music)
  (class-def :name name
	     :body
	     (list
	      "void init() { "
	      (when gravity
		(statement (fun-call "EventTest" "setGravity" gravity)))

	      (when entities
		(map 'list 
		     #'(lambda (ent)
			 (statement (fun-call "EventTest" "addEntity" ent)))
		     entities))
	      (when player
		(list player
		      
	      
	      (when (and terrainDisp terrainCol)
		(assign-statement "TerrainModel" "terDisp" "TerrainModel" (list "islandD" terrainDisp))
		(statement (add-resource "terDisp"))

		(assign-statement "Model" "terCol" "Model" (list "islandP" terrainCol))
		(statement (add-resource "terDisp"))

		(assign-statement "Terrain" "t" "Model" (list "Island"))
		(statement (add-resource "terDisp"))
		
		(statement (fun-call "t" "setDisplay" "terDisp"))
		(statement (fun-call "t" "setNavigation" "terCol"))
		(statement (fun-call "EventTest" "addEntity" "t"))
		(statement (fun-call "EventTest" "addDisplayableEntity" "t"))
		(statement (fun-call "EventTest" "setTerrain" "t")))
	     
	      (when collide
		(statement (fun-call "EventTest" "setCollideModel" collide)))

	      (when music
		(statement (fun-call "EventTest" "setPlaylist" music)))
	      "}")))

(defun entity-list (&rest ls)
  ls)

(defun print-file (string)
  (format t "~{~A~}" (flatten string)))

(defun flatten (structure)
  (cond ((null structure) nil)
        ((atom structure) `(,structure))
        (t (mapcan #'flatten structure))))

(defun fun-call (object funname args)
  (list object "." funname "(" args ")"))

(defun statement (string)
  (list string ";"))

(defun list-remove-nils (&rest list)
  (remove nil list))

(defun fun-def (&key name rettype args body)
  (list rettype " " name "(" (if args args "") ") {" body "}"))

(defun to-string (s)
  (concatenate 'string "\"" s "\""))

(defun Name (name)
  (to-string name))

(defun Model (model)
  (to-string model))

(defun to-commas (ls)
  (format nil "~{~A~^,~}" ls))

(defun Position-x-y-z (x y z)
  (to-commas (list x y z)))

(defun Terrain-Model (name)
  (to-string name))

(defun Collide-Model (name)
  (to-string name))

(defun Background-Music (&key playlist delay)
  (to-commas (list playlist delay)))

(defun Song (file)
  (to-string file))

(defun Playlist (&rest songs)
  (list "new Song[] { " (to-commas songs) "}"))

(defun Song-Delay (milliseconds)
  milliseconds)

(defun Conversation (file)
  (if t
      (to-string file)
    (with-open-file (f file)
		    (eval (read f)))))

(defun file-path (path)
  path)

(defun Player (&key pos)
  (assign-statement "Player" "player" "Player" (list))
  (statement (fun-call "player.b" "setPosition" pos))
  (statement (fun-call "EventTest" "setPlayer" "player")))

(defun assign-statement (type var-name instantiate args)
  (statement (list type var-name "=" instantiate "(" (to-commas args) ")")))

(print-file
 (Level :name "Out"
	:gravity 20
	:terrainCol (file-path "village_disp_fixed.obj")
	:terrainDisp (file-path "village_col.obj")
	:collide (Collide-Model "island-level-col.obj")
	:music (Background-Music :playlist (Playlist (Song "ding.wav")
						     (Song "jazz.wav")
						     (Song "ding.wav"))
				 :delay (Song-Delay 5000))
	:player (Player :pos (Position-x-y-z 0 10 0))
	:entities (Entity-list
		   (Person :name (Name "Martha")
			   :model (Model "martha.obj")
			   :pos (Position-x-y-z 0.5 1.0 3.1)
			   :conversation (Conversation "martha.conv"))
		   (Person :name (Name "Sebastiin")
			   :model (Model "sebastiin.obj")
			   :pos (Position-x-y-z 0.1 3.1 66.6)
			   :conversation (Conversation "sebastiin.conv")))))


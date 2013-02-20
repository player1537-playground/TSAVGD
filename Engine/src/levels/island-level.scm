;AMERICA

(level (terrain-model "island-level.obj")
       (collide-model "island-level-col.obj")
       (background-music (playlist (song "ding.wav")
                                   (song "Jazz.wav")
                                   (song "ding.wav"))
                         (song-delay 2000))
       (entity-list (person :name (name "Martha")
                            :pos (position-x-y-z 1.0 5.1 10.2)
                            :model (model "solider.obj")
                            :conversation (conversation "martha-island-level.conf"))
                    (person :name (name "Sebastiin")
                            :pos (position-x-y-z 123.0 12.1 0.5)
                            :model (model "party-hardy.obj")
                            :conversation (conversation "sebastiin-island-level.conf"))
                    (enemy :name (name "jesus")
                           :pos (position-x-y-z (+ (your-position) (position-x-y-z 0 0 50)))
                           :model (model "hey-zeus-christ-.obj"))
                    (player :name (name (get-property "player-name"))
                            :pos (get-object-position "carpet-door"))
                    (sun :name (name "SUN!!")
                         :posfun (object-position-function (* (your-position) (position-x-y-z 1 5 1)))))
       (opengl-fog :color (opengl-color 0.45f, 0.5f 0.6f 1f)
                   :density (opengl-fog-density 0.35)
                   :fog-start (opengl-fog-start 100)
                   :fog-end (opengl-fog-end 200))
       (skydome (opengl-color 0.45 0.5 0.6)))

(define person
  (lambda (:name n :pos p :model m :conversation c &opt :posfun pf)
    (when m
      (print (fun-call "this" "setModel" m)))
    (when p
      (print (fun-call "this" "setPosition" p)))
    (when pf
      (print (fun-call 
        

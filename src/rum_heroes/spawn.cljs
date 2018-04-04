(ns rum-heroes.spawn
  (:require
    [rum-heroes.config.actors :as actors]
    [rum-heroes.grid :as grid]))

;; sprites info
(def ^:const background-sprite-count 3)
(def ^:const overlay-sprite-count 3)

;; generate default background tile graphic
(defn gen-background-tile [x]
  (rand-int background-sprite-count))

;; generate default overlay tile graphic
(defn gen-overlay-tile []
  (rand-int overlay-sprite-count))

;; create overlay entity { k1 : { posX, posY, visual }}
(defn spawn-overlay [x]
  (hash-map (keyword (str "overlay" x))
            { :posX (grid/get-cell-x x)
              :posY (grid/get-cell-y x)
              :visual (gen-overlay-tile)}))

;; get random actor template
(defn get-good-template []
  (-> (count actors/actors-good-pack)
      (rand-int)
      (#(get actors/actors-good-pack %1))))

(defn get-position [n]
  (case n
    0 { :x 1 :y 1 }
    1 { :x 0 :y 2 }
    2 { :x 1 :y 3 }
    3 { :x 0 :y 4 }
    4 { :x 1 :y 5 }))

;; create actor entity 
(defn spawn-actor [x]
  (let [template (get-good-template)]
    (hash-map (keyword (str (str "army" x)))
              { :pos (get-position x)
                :hp (get-in actors/actors-template [(keyword template) :hpMax] -1)
                :template template })))
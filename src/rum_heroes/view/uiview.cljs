(ns rum-heroes.view.uiview
  (:require
    [rum-heroes.config.actors :as actors]
    [rum.core :as rum]))

;; state = tile-hover-state
(rum/defc tile-hover-component < rum/reactive [state]
  (let [cursor (rum/cursor-in state [])
        x (get @cursor 0)
        y (get @cursor 1)]
    (when (rum/react cursor)
      (if (and (>= x 0) (>= y 0))
        [:h3 (str "x: " x " y: " y)]
        [:h3 ""]))))

(rum/defc end-turn-button [on-end-turn-click]
  [:a.turnButton { :on-click on-end-turn-click } "End Turn"])

(defn get-title-class [teamId team-turn]
  (if (= teamId team-turn)
    (case teamId
      0 "title-team0-active"
      1 "title-team1-active")
    (case teamId
      0 "title-team0"
      1 "title-team1")))


(rum/defc player-title < rum/reactive [name teamId team-turn] 
  (let [cursor (rum/cursor-in team-turn [])]
    (when (rum/react cursor)
      [:div { :class (get-title-class teamId @team-turn) }
              name (when (= teamId @team-turn) " moving...")])))

(defn get-actor [selected hover teamId]
  (if (and (not (empty? hover)) (= teamId (get hover :teamId)))
    hover 
    selected))

(defn get-actor-body [actor teamId]
  (when (and (not (empty? actor)) (= teamId (get actor :teamId)))
    (let [t (actors/get-template actor)]
      [:div.actor-panel-ui
        [ :div.actor-avatar
          [ :div.actor-image { :class (get t :visual) }]]
        [ :div.actor-params
          [ :div (get actor :template)]
          [ :div "damage: " (get t :damage)]
          [ :div "range: " (get t :range)]
          [ :div "moves: " (get t :moves)]
          [ :div "hp: " (get t :hpMax)]]])))

;; state -- derived atom with hover and selected state
(rum/defc actor-ui-component < rum/reactive [state teamId]
  (let [ s (rum/react state)
         hover (first (get s 0))
         selected (get s 1)]
    (get-actor-body (get-actor selected hover teamId) teamId)))

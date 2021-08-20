(ns app
  (:require
   [clojure.pprint :as pp]
   [db]
   [devtools]
   [routing]
   [re-frame.core :as rf]
   [reagent.dom :as d]))

(defn header
  []
  [:nav
   [:h1 "Clojial"]
   [:ul
    [:li
     [:a {:href ""} "home"]]
    [:li
     [:a {:href ""} "archive"]]]])

(defn post-card
  [post]
  [:div
   [:a.header-link (get post :name)]
   [:h3 {:style {:margin-top 0}} (-> post :id str)]
   [:p (get post :description)]])

(defn post-cards
  []
  (let [posts @(rf/subscribe [:posts])]
    [:ul
     (map (fn [p] ^{:key (get p :id)} [:li [post-card p]]) posts)]))

(defn intro
  []
  [:p "Welcome to the clojial blog. This is my place to write about my experience writing software (mostly about clojure)."])

(defn show-state
  []
  (let [state @(rf/subscribe [:db])]
   [:div
    [:h1 "State"]
    [:p {:style {:white-space "pre-wrap"}} (with-out-str (pp/pprint state))]]))

(defn app
  []
  [:div
   [header]
   [intro]
   [post-cards]
   [show-state]])

(defn ^:dev/after-load run-app
  []
  (d/render [app]
            (.getElementById js/document "root")))

(defn ^:export main
  []
  (rf/dispatch-sync [:initialize])
  (run-app))

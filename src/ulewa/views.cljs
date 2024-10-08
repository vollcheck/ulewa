(ns ulewa.views
  (:require
   [clojure.string :as str]
   [goog.string :as gstring]
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch subscribe]]
   [ulewa.events :as events]
   [ulewa.subs :as subs]
   ))

(defn city-input [{:keys [title on-save on-stop]}]
  (let [val (reagent/atom title)
        stop #(do (reset! val "")
                  (when on-stop (on-stop)))
        save #(let [v (-> @val str str/trim)]
                 (on-save v)
                 (stop))]
    (fn [props]
      [:input (merge (dissoc props :on-save :on-stop :title)
                     {:type "text"
                      :value @val
                      :auto-focus true
                      :on-blur save
                      :on-change #(reset! val (-> % .-target .-value))
                      :on-key-down #(case (.-which %)
                                      13 (save)
                                      27 (stop)
                                      nil)})])))

(defn city-entry []
  [city-input
   {:id "search-city"
    :placeholder "city name"
    :on-save #(when (seq %)
                (dispatch [::events/fetch-city %]))}])

(defn city-main-view [{:keys [name lat lon country state]}]
  [:<>
   [:div
    [:p "name: " name]
    [:p "lat/lon: " (gstring/format "(%.2f/%.2f)" lat lon)]
    [:p "country: " country]
    [:p "state: " state]]])

(defn k2c [k]
  (- k 273.15))

(defn weather-view [{:keys [weather main]}]
  (let [{:keys [pressure temp feels_like humidity]} main]
    [:<>
     [:div
      [:p "weather: " (-> weather first :description)]
      [:p "temp: " (gstring/format "%.2f" (k2c temp))]
      [:p "feels like temp: " (gstring/format "%.2f" (k2c feels_like))]
      [:p "pressure: " pressure " hPa"]
      [:p "humidity: " humidity]]]))

(defn main-panel []
  #_[:div [:h1 "Hello"]]
  (let [loading (subscribe [::subs/loading])
        city-data (subscribe [::subs/city-data])
        weather-data (subscribe [::subs/city-weather])]
    [:div
     [:h1
      "Hello from " (if @city-data
                      (str (:name @city-data) "!")
                      "nowhere...")]
     [city-entry]
     [:div
      (cond
        @loading "Downloading data..."
        @city-data [:div
                    [city-main-view @city-data]
                    [weather-view @weather-data]]
        :else nil)]]))

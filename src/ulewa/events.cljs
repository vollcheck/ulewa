(ns ulewa.events
  (:require
   [re-frame.core :as rf]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   [ulewa.config :as config]
   [ulewa.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(def default-fetch
  {:method :get
   :timeout 8000
   :response-format (ajax/json-response-format {:keywords? true})})

(rf/reg-event-fx
 ::fetch-city
 (fn [{:keys [db]} [_ city-name]]
   (js/console.log "fetching city with for name: " city-name)
   (js/console.log "fetching city with for name: " config/api-key)
   {:db (assoc db :loading true)
    :http-xhrio (merge default-fetch
                       {:uri config/geo-api-url
                        :params {"q" city-name
                                 "limit" 1
                                 "appid" config/api-key}
                        :on-success [::fetch-city-success]
                        :on-failure [:bad-http-result]})}))

(rf/reg-event-fx
 ::fetch-city-success
 (fn [db [_ city-data]]
   (let [record (-> (first city-data)
                    (dissoc :local_names))]
     (js/console.log "record: " record)
     {:db (assoc db :city-data record)
      :dispatch [::fetch-city-weather record]})))

(rf/reg-event-fx
 ::fetch-city-weather
 (fn [{:keys [db]} [_ {:keys [lat lon] :as record}]]
   (js/console.log "fetching city wather for city: " (:name record))
   {:http-xhrio (merge default-fetch
                       {:uri config/weather-api-url
                        :params {"lat" lat
                                 "lon" lon
                                 "appid" config/api-key}
                        :on-success [::fetch-weather-success]
                        :on-failure [::bad-http-result]})}))

(rf/reg-event-db
 ::fetch-weather-success
 (fn [db [_ city-weather]]
   (js/console.log "city-weather: " city-weather)
   (assoc db
          :loading false
          :city-weather city-weather)))


(rf/reg-event-fx
 ::bad-http-result
 (fn [_ value]
   (js/console.log "didn't managed to download the data: " value)))

(ns starter.config
  (:require [clojure.string :as str]))

(def debug?
  ^boolean goog.DEBUG)

(def api-key
  (-> "resources/API_KEY" slurp str/trim))

(def geo-api-url
  "http://api.openweathermap.org/geo/1.0/direct")

(def weather-api-url
  "https://api.openweathermap.org/data/2.5/weather")

(def limit 1)

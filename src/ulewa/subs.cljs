(ns ulewa.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::loading
 (fn [db]
   (:loading db)))

(re-frame/reg-sub
 ::city-data
 (fn [db]
   (:city-data db)))

(re-frame/reg-sub
 ::city-weather
 (fn [db]
   (:city-weather db)))

(comment
  (defmacro defsub [sub-name k]
  `(re-frame/reg-sub
    ~sub-name
    (fn [db]
      (~k db))))

  (defsub ::loading :loading)
  (defsub ::city-data :city-data)
  )

(ns Clock
  (:require
    [salt.lang :refer [VARIABLE always- mod*]]
    [tlaplus.Integers]))

(VARIABLE clock)

(defn TypeOk []
  (contains? #{0 1} clock))

(defn Init []
  (or (= clock 0)
      (= clock 1)))

(defn Tick []
  (or (and (= clock 0)
           (= clock' 1))
      (and (= clock 1)
           (= clock' 0))))

(defn Tick2 []
  (= clock'
     (if (= clock 0)
       1
       0)))

(defn Tick3 []
  (= clock'
     (mod* (+ 1 clock) 2)))

(defn Spec []
  (and
   (Init)
   (always- (Tick) [clock])))

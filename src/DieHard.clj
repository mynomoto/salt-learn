(ns DieHard
  (:require
    [salt.lang :refer [VARIABLE range*]]
    [tlaplus.Integers]))

(VARIABLE big small)

(defn Init []
  (and (= big 0)
       (= small 0)))

(defn FillSmall []
  (and (= small' 3)
       (= big' big)))

(defn FillBig []
  (and (= big' 5)
       (= small' small)))

(defn EmptySmall []
  (and (= small' 0)
       (= big' big)))

(defn EmptyBig []
  (and (= big' 0)
       (= small' small)))

(defn Min [m n]
  (if (< m n)
    m
    n))

(defn SmallToBig []
  (let [poured (- (Min (+ big small) 5) big)]
    (and
      (= big' (+ big poured))
      (= small' (- small poured)))))

(defn BigToSmall []
  (let [poured (- (Min (+ big small) 3) small)]
    (and
      (= small' (+ small poured))
      (= big' (- big poured)))))

(defn Next []
  (or (FillSmall)
      (FillBig)
      (EmptySmall)
      (EmptyBig)
      (SmallToBig)
      (BigToSmall)))

(defn TypeOk []
  (and (contains? (range* 0 5) big)
       (contains? (range* 0 3) small)))

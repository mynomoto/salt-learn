(ns Wire
  (:require
    [salt.lang :refer :all]
    [tlaplus.Integers]))

(VARIABLE people acc sender receiver amount op)

(defn Init []
  (and (= people #{"alice" "bob"})
       (= acc (fm- [a people] 5))
       (= sender "alice")
       (= receiver "bob")
       (= amount 3)
       (= op "withdraw")))

(defn NoOverdrafts []
  (A [p people]
    (>= (get* acc p) 0)))

(defn Withdraw []
  (and (= op "withdraw")
       (= acc' (EXCEPT acc [sender] (- (get* acc sender) amount)))
       (= op' "deposit")
       (CHANGED- [acc op])))

(defn Deposit []
  (and (= op "deposit")
       (= acc' (EXCEPT acc [receiver] (+ (get* acc receiver) amount)))
       (= op' "done")
       (CHANGED- [acc op])))

(defn Terminating []
  (and (= op "done")
       (CHANGED- [])))

(defn Next []
  (or (Withdraw)
      (Deposit)
      (Terminating)))

(defn Spec []
  (and
    (Init)
    (always- (Next) VARS-)))

(defn End []
  (= op "done"))

(defn Termination []
  (eventually- (End)))

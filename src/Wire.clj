(ns Wire
  (:require
    [salt.lang :refer :all]
    [tlaplus.Integers]))

(VARIABLE people acc pc sender receiver amount)

(def vars VARS-)

(defn NoOverdrafts []
  (A [p people]
    (>= (get* acc p) 0)))

(defn EventuallyConsistent []
  (eventually- (always- (= (+ (get* acc "alice") (get* acc "bob")) 10))))

(defn ProcSet []
  (range* 1 2))

(defn Init []
  (and
    (= people #{"alice" "bob"})
    (= acc (fm- [p people] 5))
    (= sender (fm- [self (ProcSet)] "alice"))
    (= receiver (fm- [self (ProcSet)] "bob"))
    (contains? (maps- (ProcSet) (range* 1 (get* acc (get* sender (CHOOSE [self (ProcSet)] true))))) amount)
    (= pc (fm- [self (ProcSet)] "CheckAndWithdraw"))))

(defn CheckAndWithdraw [self]
  (and
    (= (get* pc self) "CheckAndWithdraw")
    (if (<= (get* amount self) (get* acc (get* sender self)))
      (and
        (= acc' (EXCEPT acc [(get* sender self)] (- (get* acc (get* sender self)) (get* amount self))))
        (= pc' (EXCEPT pc [self] "Deposit")))
      (and
        (= acc' acc)
        (= pc' (EXCEPT pc [self] "Done"))))
    (CHANGED- [acc pc])))

(defn Deposit [self]
  (and
    (= (get* pc self) "Deposit")
    (= acc' (EXCEPT acc [(get* receiver self)] (+ (get* acc (get* receiver self)) (get* amount self))))
    (= pc' (EXCEPT pc [self] "Done"))
    (CHANGED- [acc pc])))

(defn Wire [self]
  (or
    (CheckAndWithdraw self)
    (Deposit self)))

(defn Terminating []
  (and
    (A [self (ProcSet)]
      (= (get* pc self) "Done"))
    (UNCHANGED vars)))

(defn Next []
  (or
    (E [self (ProcSet)]
      (Wire self))
    (Terminating)))

(defn Spec []
  (and
    (Init)
    (always- (Next) vars)))

(defn Termination []
  (eventually-
    (A [self (ProcSet)]
      (= (get* pc self) "Done"))))

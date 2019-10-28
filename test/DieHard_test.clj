(ns DieHard-test
  (:require
    [DieHard :refer :all]
    [clojure.test :refer [are deftest is run-tests use-fixtures]]
    [salt]))

(use-fixtures :each (salt/namespace-fixture 'DieHard-test))

(deftest test-init
  (is (salt/evaluate
        {}
        '{big 0
          small 0}
        (Init)))
  (is (not (salt/evaluate
             {}
             '{big 1
               small 0}
             (Init))))
  (is (not (salt/evaluate
             {}
             '{big 0
               small 1}
             (Init)))))

(deftest test-TypeOk
  (are [b s result]
    (= result
       (salt/evaluate
         {}
         {'big b
          'small s}
         (TypeOk)))
    0 0 true
    0 1 true
    0 2 true
    0 3 true
    1 0 true
    1 1 true
    1 2 true
    1 3 true
    2 0 true
    2 1 true
    2 2 true
    2 3 true
    3 0 true
    3 1 true
    3 2 true
    3 3 true
    4 0 true
    4 1 true
    4 2 true
    4 3 true
    5 0 true
    5 1 true
    5 2 true
    5 3 true
    6 3 false
    5 4 false
    6 4 false))

(deftest test-FillSmall
  (are [s result]
    (= result
       (salt/simplify "src/DieHard.clj"
         {}
         {'big 0
          'small s}
         (FillSmall)
         :delta))
    0 '{small' 3}
    1 '{small' 3}
    2 '{small' 3}
    3 {}))

(deftest test-FillBig
  (are [b result]
    (= result
       (salt/simplify "src/DieHard.clj"
         {}
         {'big b
          'small 0}
         (FillBig)
         :delta))
    0 '{big' 5}
    1 '{big' 5}
    2 '{big' 5}
    3 '{big' 5}
    4 '{big' 5}
    5 {}))

(deftest test-EmptySmall
  (are [s result]
    (= result
       (salt/simplify "src/DieHard.clj"
         {}
         {'big 0
          'small s}
         (EmptySmall)
         :delta))
    0 '{}
    1 '{small' 0}
    2 '{small' 0}
    3 '{small' 0}))

(deftest test-EmptyBig
  (are [b result]
    (= result
       (salt/simplify "src/DieHard.clj"
         {}
         {'big b
          'small 0}
         (EmptyBig)
         :delta))
    0 {}
    1 '{big' 0}
    2 '{big' 0}
    3 '{big' 0}
    4 '{big' 0}
    5 '{big' 0}))

(deftest test-Min
  (are [m n result]
    (= result (Min m n))
    0 0 0
    1 0 0
    0 1 0
    1 1 1
    1 9 1
    9 0 0
    9 9 9))

(deftest test-SmallToBig
  (are [s b result]
    (= result
       (salt/simplify "src/DieHard.clj"
         {}
         {'big b
          'small s}
         (SmallToBig)
         :delta))
  0 0 {}
  0 1 {}
  0 2 {}
  0 3 {}
  0 4 {}
  0 5 {}
  1 0 '{small' 0 big' 1}
  1 1 '{small' 0 big' 2}
  1 2 '{small' 0 big' 3}
  1 3 '{small' 0 big' 4}
  1 4 '{small' 0 big' 5}
  1 5 {}
  2 0 '{small' 0 big' 2}
  2 1 '{small' 0 big' 3}
  2 2 '{small' 0 big' 4}
  2 3 '{small' 0 big' 5}
  2 4 '{small' 1 big' 5}
  2 5 {}
  3 0 '{small' 0 big' 3}
  3 1 '{small' 0 big' 4}
  3 2 '{small' 0 big' 5}
  3 3 '{small' 1 big' 5}
  3 4 '{small' 2 big' 5}
  3 5 {}))

(deftest test-BigToSmall
  (are [b s result]
    (= result
       (salt/simplify "src/DieHard.clj"
         {}
         {'big b
          'small s}
         (BigToSmall)
         :delta))
  0 0 {}
  0 1 {}
  0 2 {}
  0 3 {}
  1 0 '{big' 0 small' 1}
  1 1 '{big' 0 small' 2}
  1 2 '{big' 0 small' 3}
  1 3 {}
  2 0 '{big' 0 small' 2}
  2 1 '{big' 0 small' 3}
  2 2 '{big' 1 small' 3}
  2 3 {}
  3 0 '{big' 0 small' 3}
  3 1 '{big' 1 small' 3}
  3 2 '{big' 2 small' 3}
  3 3 {}
  4 0 '{big' 1 small' 3}
  4 1 '{big' 2 small' 3}
  4 2 '{big' 3 small' 3}
  4 3 {}
  5 0 '{big' 2 small' 3}
  5 1 '{big' 3 small' 3}
  5 2 '{big' 4 small' 3}
  5 3 {}))

(comment
  (run-tests))

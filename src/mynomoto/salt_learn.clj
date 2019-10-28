(ns mynomoto.salt-learn
  (:require
    salt))

(comment
  (spit "DieHard.tla" (salt/transpile "src/DieHard.clj"))
  (spit "Wire.tla" (salt/transpile "src/Wire.clj"))
  (spit "Clock.tla" (salt/transpile "src/Clock.clj")))

(ns bezier-generator.core
  (:require [thi.ng.geom.viz.core :as viz]
            [thi.ng.geom.svg.core :as svg]
            [thi.ng.geom.core.vector :as v]
            [thi.ng.color.core :as col]
            [thi.ng.math.core :as m :refer [PI TWO_PI]]))


(defn export-viz
  [spec path]
  (->> spec
       (viz/svg-plot2d-cartesian)
       (svg/svg {:width 600 :height 600})
       (svg/serialize)
       (spit path)))

(defn random-scalar ;scalar turned down for testing.
  []
  (let [bool (rand-nth [true false])]
        (if bool 
          (rand-nth (range 1 2))
          (rand-nth (map #(/ % 2) (range 1 2))))))

(random-scalar)
(defmacro rand-op
  []
  '(rand-nth ['+ '*]))

(defmacro rand-wave
  []
  '(list '* (random-scalar) (list (rand-nth ['Math/sin 'Math/cos]) (list '* (random-scalar) 'x))))

(rand-wave)
;switch apply to cons for better results
(defmacro function-generator
  []
  '(list 'fn '[x] (cons (rand-op) (repeatedly (rand-nth (range 1 6)) #(rand-wave)))))

(function-generator)
(eval 5)
(let [functions (map eval (repeatedly 2 #(function-generator)))]
  (map #(%1 %2) functions [1 2]))
(map eval (repeatedly 2 #(function-generator)))
(repeatedly 1 #(function-generator))
(def spec
  {:x-axis (viz/linear-axis
            {:domain [-1.8 1.8]
             :range  [50 590]
             :pos    -1})
   :y-axis (viz/linear-axis
            {:domain      [-1.8 1.8]
             :range       [550 20]
             :pos         -1})
   :grid   {:attribs {:stroke "#caa"}
            :minor-x false
            :minor-y false}
   :data   [{:values  (map (juxt (eval (function-generator)) (eval (function-generator))) 
                           (range 0 300 0.01))
             :attribs {:fill "#0af" :stroke "none"}
             :layout  viz/svg-scatter-plot}
            ]})
(fn [x] (* (* 1 (Math/cos (* 1/2 x))) (* 1 (Math/sin (* 1 x))) (* 1 (Math/cos (* 1 x)))))
(eval) (function-generator)
(export-viz spec "bezier.svg")
(-> spec
    (assoc :y-axis (viz/log-axis
                    {:domain      [0.1 101]
                     :range       [550 20]
                     :pos         50
                     :label-dist  15
                     :label-style {:text-anchor "end"}}))
    (export-viz "scatter-log.svg"))

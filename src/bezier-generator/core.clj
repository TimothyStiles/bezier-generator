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

(def spec
  {:x-axis (viz/linear-axis
            {:domain [-2 2]
             :range  [50 590]
             :pos    -1})
   :y-axis (viz/linear-axis
            {:domain      [-1 1]
             :range       [550 20]
             :pos         -1})
   :grid   {:attribs {:stroke "#caa"}
            :minor-x false
            :minor-y false}
   :data   [{:values  (map (juxt #(- (Math/sin %) (Math/sin (* 2.3 %))) #(Math/cos %)) (range 0 200 0.01))
             :attribs {:fill "#0af" :stroke "none"}
             :layout  viz/svg-scatter-plot}
            ]})

(export-viz spec "bezier.svg")
(-> spec
    (assoc :y-axis (viz/log-axis
                    {:domain      [0.1 101]
                     :range       [550 20]
                     :pos         50
                     :label-dist  15
                     :label-style {:text-anchor "end"}}))
    (export-viz "scatter-log.svg"))

(ns starter.spline
  "Canvas-based spline interpolation library
  
  by George MacKerron, mackerron.com
  
  Monotonic Cubic Spline:
  
   adapted from:
      http://sourceforge.net/mailarchive/forum.php?thread_name=EC90C5C6-C982-4F49-8D46-A64F270C5247%40gmail.com&forum_name=matplotlib-users
      (easier to read at http://old.nabble.com/%22Piecewise-Cubic-Hermite-Interpolating-Polynomial%22-in-python-td25204843.html)
  
   with help from:
       F N Fritsch & R E Carlson (1980) 'Monotone Piecewise Cubic Interpolation', SIAM Journal of Numerical Analysis 17(2), 238 - 246.
       http://en.wikipedia.org/wiki/Monotone_cubic_interpolation
       http://en.wikipedia.org/wiki/Cubic_Hermite_spline
  
  
   Natural and Clamped:
  
   adapted from:
       http://www.michonline.com/ryan/csc/m510/splinepresent.html")

;; =============================================================================
;; MonotonicCubicSpline
;; =============================================================================

(defn monotonic-cubic-spline
  "Create a monotonic cubic spline interpolator from x and y arrays"
  [x y]
  (let [n     (count x)
        delta (array)
        m     (array)
        alpha (array)
        beta  (array)
        dist  (array)
        tau   (array)]
    
    ;; Calculate deltas and initial slopes
    (doseq [i (range (dec n))]
      (aset delta i (/ (- (aget y (inc i)) (aget y i))
                       (- (aget x (inc i)) (aget x i))))
      (when (pos? i)
        (aset m i (/ (+ (aget delta (dec i)) (aget delta i)) 2))))
    
    (aset m 0 (aget delta 0))
    (aset m (dec n) (aget delta (- n 2)))
    
    ;; Find points where delta is zero
    (let [to-fix (atom [])]
      (doseq [i (range (dec n))]
        (when (zero? (aget delta i))
          (swap! to-fix conj i)))
      
      ;; Fix those points
      (doseq [i @to-fix]
        (aset m i 0)
        (aset m (inc i) 0)))
    
    ;; Calculate alpha, beta, distance, and tau
    (doseq [i (range (dec n))]
      (aset alpha i (/ (aget m i) (aget delta i)))
      (aset beta i (/ (aget m (inc i)) (aget delta i)))
      (aset dist i (+ (js/Math.pow (aget alpha i) 2)
                      (js/Math.pow (aget beta i) 2)))
      (aset tau i (/ 3 (js/Math.sqrt (aget dist i)))))
    
    ;; Find points where distance > 9
    (let [to-fix (atom [])]
      (doseq [i (range (dec n))]
        (when (> (aget dist i) 9)
          (swap! to-fix conj i)))
      
      ;; Fix those points
      (doseq [i @to-fix]
        (aset m i (* (aget tau i) (aget alpha i) (aget delta i)))
        (aset m (inc i) (* (aget tau i) (aget beta i) (aget delta i)))))
    
    ;; Return interpolator function with stored data
    {:x           (vec (take n x))
     :y           (vec (take n y))
     :m           (vec m)
     :interpolate (fn [x-val]
                    (let [x-vec (vec (take n x))
                          y-vec (vec (take n y))
                          m-vec (vec m)]
         ;; Find the appropriate segment
                      (loop [i (- n 2)]
                        (if (or (<= i 0) (<= (nth x-vec i) x-val))
                          (let [i   (max 0 i)
                                h   (- (nth x-vec (inc i)) (nth x-vec i))
                                t   (/ (- x-val (nth x-vec i)) h)
                                t2  (js/Math.pow t 2)
                                t3  (js/Math.pow t 3)
                                h00 (+ (* 2 t3) (* -3 t2) 1)
                                h10 (+ t3 (* -2 t2) t)
                                h01 (+ (* -2 t3) (* 3 t2))
                                h11 (- t3 t2)
                                y   (+ (* h00 (nth y-vec i))
                                       (* h10 h (nth m-vec i))
                                       (* h01 (nth y-vec (inc i)))
                                       (* h11 h (nth m-vec (inc i))))]
                            y)
                          (recur (dec i))))))}))

;; =============================================================================
;; CubicSpline
;; =============================================================================

(defn cubic-spline
  "Create a cubic spline interpolator (natural or clamped)
  x - array of x coordinates
  a - array of y coordinates (function values)
  d0 - (optional) derivative at first point for clamped spline
  dn - (optional) derivative at last point for clamped spline"
  ([x a] (cubic-spline x a nil nil))
  ([x a d0 dn]
   (when (and x a)
     (let [clamped (and (some? d0) (some? dn))
           n (dec (count x))
           h (array)
           y (array)
           l (array)
           u (array)
           z (array)
           c (array)
           b (array)
           d (array)
           k (array)
           s (array)]
       
       ;; Calculate h, k, and s
       (doseq [i (range n)]
         (aset h i (- (aget x (inc i)) (aget x i)))
         (aset k i (- (aget a (inc i)) (aget a i)))
         (aset s i (/ (aget k i) (aget h i))))
       
       ;; Initialize y values for clamped case
       (when clamped
         (aset y 0 (- (/ (* 3 (- (aget a 1) (aget a 0))) (aget h 0))
                      (* 3 d0)))
         (aset y n (- (* 3 dn)
                      (/ (* 3 (- (aget a n) (aget a (dec n))))
                         (aget h (dec n))))))
       
       ;; Calculate intermediate y values
       (doseq [i (range 1 n)]
         (aset y i (- (/ (* 3 (- (aget a (inc i)) (aget a i))) (aget h i))
                      (/ (* 3 (- (aget a i) (aget a (dec i)))) (aget h (dec i))))))
       
       ;; Initialize l, u, z for clamped or natural case
       (if clamped
         (do
           (aset l 0 (* 2 (aget h 0)))
           (aset u 0 0.5)
           (aset z 0 (/ (aget y 0) (aget l 0))))
         (do
           (aset l 0 1)
           (aset u 0 0)
           (aset z 0 0)))
       
       ;; Forward elimination
       (doseq [i (range 1 n)]
         (aset l i (- (* 2 (- (aget x (inc i)) (aget x (dec i))))
                      (* (aget h (dec i)) (aget u (dec i)))))
         (aset u i (/ (aget h i) (aget l i)))
         (aset z i (/ (- (aget y i) (* (aget h (dec i)) (aget z (dec i))))
                      (aget l i))))
       
       ;; Finalize l, z, c for clamped or natural case
       (if clamped
         (do
           (aset l n (* (aget h (dec n)) (- 2 (aget u (dec n)))))
           (aset z n (/ (- (aget y n) (* (aget h (dec n)) (aget z (dec n))))
                        (aget l n)))
           (aset c n (aget z n)))
         (do
           (aset l n 1)
           (aset z n 0)
           (aset c n 0)))
       
       ;; Back substitution
       (doseq [i (range (dec n) -1 -1)]
         (aset c i (- (aget z i) (* (aget u i) (aget c (inc i)))))
         (aset b i (- (/ (- (aget a (inc i)) (aget a i)) (aget h i))
                      (/ (* (aget h i) (+ (aget c (inc i)) (* 2 (aget c i)))) 3)))
         (aset d i (/ (- (aget c (inc i)) (aget c i)) (* 3 (aget h i)))))
       
       ;; Return spline object with methods
       {:x (vec (take (inc n) x))
        :a (vec (take n a))
        :b (vec b)
        :c (vec (take n c))
        :d (vec d)
        
        :derivative
        (fn [self]
          (let [s {:x (vec (:x self))
                   :a (vec (:b self))
                   :b (mapv #(* 2 %) (:c self))
                   :c (mapv #(* 3 %) (:d self))
                   :d (vec (repeat (count (:d self)) 0))}]
            s))
        
        :interpolate
        (fn [self x-val]
          (let [x-vec (:x self)
                a-vec (:a self)
                b-vec (:b self)
                c-vec (:c self)
                d-vec (:d self)]
            ;; Find the appropriate segment
            (loop [i (dec (count x-vec))]
              (if (or (<= i 0) (<= (nth x-vec i) x-val))
                (let [i (max 0 i)
                      delta-x (- x-val (nth x-vec i))
                      y (+ (nth a-vec i)
                           (* (nth b-vec i) delta-x)
                           (* (nth c-vec i) (js/Math.pow delta-x 2))
                           (* (nth d-vec i) (js/Math.pow delta-x 3)))]
                  y)
                (recur (dec i))))))}))))

;; =============================================================================
;; PointCollection
;; =============================================================================

(defn point-collection
  "Create a new point collection for managing control points"
  []
  (let [state (atom {:points []
                     :min {:x 0 :y 0}
                     :max {:x js/Infinity :y js/Infinity}})]
    
    {:state state
     
     ;; Define the acceptable min and max for both axis.
     ;; bound - can be "min" or "max"
     ;; axis - can be "x" or "y"
     ;; value - a number
     ;; Note that minimum boundaries are inclusive while max are exclusive
     :set-boundary
     (fn [bound axis value]
       (swap! state assoc-in [(keyword bound) (keyword axis)] value))
     
     ;; Add a point to the collection
     :add
     (fn [p]
       (let [{:keys [min max]} @state
             x (:x p)
             y (:y p)]
         (when (and (>= x (:x min)) (<= x (:x max))
                    (>= y (:y min)) (<= y (:y max)))
           (let [p (-> p
                       (assoc :x-locked (get p :x-locked false))
                       (assoc :y-locked (get p :y-locked false))
                       (assoc :safe (get p :safe false)))
                 points (:points @state)]
             
             ;; Find insertion index (sorted by x)
             (if (empty? points)
               (do
                 (swap! state update :points conj p)
                 0)
               (let [insert-idx (loop [i 0]
                                  (cond
                                    (>= i (count points)) i
                                    (< x (:x (nth points i))) i
                                    :else (recur (inc i))))]
                 (swap! state update :points
                        (fn [pts]
                          (vec (concat (take insert-idx pts)
                                       [p]
                                       (drop insert-idx pts)))))
                 insert-idx))))))
     
     ;; Remove a point by index
     :remove
     (fn [idx]
       (when (and idx (>= idx 0) (< idx (count (:points @state))))
         (let [point (nth (:points @state) idx)]
           (when-not (:safe point)
             (swap! state update :points
                    (fn [pts]
                      (vec (concat (take idx pts) (drop (inc idx) pts)))))
             point))))
     
     ;; Get the index of a point at given coordinates (with tolerance)
     :get-at
     (fn [x y tolerance]
       (let [points (:points @state)]
         (loop [i 0]
           (when (< i (count points))
             (let [pt (nth points i)
                   dx (js/Math.abs (- x (:x pt)))
                   dy (js/Math.abs (- y (:y pt)))]
               (if (and (<= dx tolerance) (<= dy tolerance))
                 i
                 (recur (inc i))))))))
     
     ;; Get a point by index
     :get
     (fn [idx]
       (when (and idx (>= idx 0) (< idx (count (:points @state))))
         (nth (:points @state) idx)))
     
     ;; Get the number of points
     :size
     (fn []
       (count (:points @state)))
     
     ;; Get all x coordinates
     :get-x-series
     (fn []
       (mapv :x (:points @state)))
     
     ;; Get all y coordinates
     :get-y-series
     (fn []
       (mapv :y (:points @state)))
     
     ;; Update a point's position
     :update-point
     (fn [idx x y]
       (when (and idx (>= idx 0) (< idx (count (:points @state))))
         (let [point (nth (:points @state) idx)
               {:keys [min max]} @state]
           (when (and (>= x (:x min)) (<= x (:x max))
                      (>= y (:y min)) (<= y (:y max)))
             (let [new-point (-> point
                                 (assoc :x (if (:x-locked point) (:x point) x))
                                 (assoc :y (if (:y-locked point) (:y point) y)))
                   points (:points @state)
                   without-point (vec (concat (take idx points) (drop (inc idx) points)))]
               
               ;; Find new insertion index
               (let [insert-idx (loop [i 0]
                                  (cond
                                    (>= i (count without-point)) i
                                    (< (:x new-point) (:x (nth without-point i))) i
                                    :else (recur (inc i))))]
                 (swap! state assoc :points
                        (vec (concat (take insert-idx without-point)
                                     [new-point]
                                     (drop insert-idx without-point))))
                 insert-idx))))))}))

;; =============================================================================
;; CanvasSpliner
;; =============================================================================

(defn canvas-spliner
  "Create a new CanvasSpliner instance
  canvas-id - the DOM id of the canvas element
  options - optional configuration map"
  ([canvas-id] (canvas-spliner canvas-id {}))
  ([canvas-id options]
   (let [canvas (js/document.getElementById canvas-id)
         ctx (.getContext canvas "2d")
         screen-ratio (or (.-devicePixelRatio js/window) 1)
         width (.-width canvas)
         height (.-height canvas)
         
         ;; Default options
         defaults {:interpolation-mode "MonotonicCubic"
                   :background-color "#222222"
                   :text-color "#ffffff"
                   :curve-color {:idle "#2ad4ff" :moving "#ff2a7f"}
                   :control-point-color {:idle "#2ad4ff" :hovered "#55ff7f" :grabbed "#ff2a7f"}
                   :grid-color "#555555"
                   :grid-step 0.1
                   :control-point-radius 6
                   :curve-thickness 2
                   :border-style {:in "2px solid #2ad4ff" :out "2px solid #888888"}}
         
         opts (merge defaults options)
         
         ;; Create spline constructor based on mode
         spline-constructor (case (:interpolation-mode opts)
                              "Cubic" cubic-spline
                              "MonotonicCubic" monotonic-cubic-spline
                              monotonic-cubic-spline)
         
         point-coll (point-collection)
         
         state (atom {:canvas canvas
                      :ctx ctx
                      :width width
                      :height height
                      :screen-ratio screen-ratio
                      :point-collection point-coll
                      :spline-constructor spline-constructor
                      :mouse nil
                      :mouse-down false
                      :point-hovered-index -1
                      :point-grabbed-index -1
                      :x-series-interpolated (array)
                      :y-series-interpolated (array)
                      :on-events {}
                      :options opts})]
     
     ;; Set canvas style and size
     (set! (.-style canvas) (.-style canvas))
     (.setAttribute canvas "width" (* width screen-ratio))
     (.setAttribute canvas "height" (* height screen-ratio))
     (set! (.. canvas -style -width) (str width "px"))
     (set! (.. canvas -style -height) (str height "px"))
     (.scale ctx screen-ratio screen-ratio)
     
     ;; Set boundaries on point collection
     ((:set-boundary point-coll) "min" "x" 0)
     ((:set-boundary point-coll) "max" "x" width)
     ((:set-boundary point-coll) "min" "y" 0)
     ((:set-boundary point-coll) "max" "y" height)
     
     ;; Helper functions
     (letfn [(get-mouse-pos [evt]
               (let [rect (.getBoundingClientRect canvas)
                     x (- (.-clientX evt) (.-left rect))
                     y (- (.-clientY evt) (.-top rect))]
                 {:x (* x screen-ratio) :y (* y screen-ratio)}))
             
             ;; [PRIVATE]
             ;; Paint the background with a given color
             (fill-background! []
               (when-let [bg-color (get-in @state [:options :background-color])]
                 (.beginPath ctx)
                 (.rect ctx 0 0 width height)
                 (set! (.-fillStyle ctx) bg-color)
                 (.fill ctx)))
             
             ;; [PRIVATE]
             ;; Display xy coordinates on the upper left corner
             (draw-coordinates! [x y]
               (let [text-size (/ 14 screen-ratio)]
                 (set! (.-fillStyle ctx) (get-in @state [:options :text-color]))
                 (set! (.-font ctx) (str text-size "px courier"))
                 (.fillText ctx (str "x: " x) (/ 10 screen-ratio) (/ 20 screen-ratio))
                 (.fillText ctx (str "y: " y) (/ 10 screen-ratio) (/ 35 screen-ratio))))
             
             ;; [PRIVATE]
             ;; Draw the background grid
             (draw-grid! []
               (let [step (get-in @state [:options :grid-step])]
                 (when-not (zero? step)
                   (.beginPath ctx)
                   (.moveTo ctx 0 0)
                   
                   ;; Horizontal grid lines
                   (doseq [i (range (js/Math.floor (/ 1 step)))]
                     (let [y (+ (* step height (inc i)) (/ 0.5 screen-ratio))]
                       (.moveTo ctx 0 y)
                       (.lineTo ctx width y)))
                   
                   ;; Vertical grid lines
                   (.moveTo ctx 0 0)
                   (doseq [i (range (js/Math.floor (/ 1 step)))]
                     (let [x (+ (* step width (inc i)) (/ 0.5 screen-ratio))]
                       (.moveTo ctx x 0)
                       (.lineTo ctx x height)))
                   
                   (set! (.-strokeStyle ctx) (get-in @state [:options :grid-color]))
                   (set! (.-lineWidth ctx) 0.5)
                   (.stroke ctx)
                   (.closePath ctx))))
             
             ;; [PRIVATE]
             ;; Draw the data on the canvas
             ;; curve - whether or not we draw the curve
             ;; control - whether or not we draw the control points
             (draw-data! [& {:keys [curve control] :or {curve true control true}}]
               (let [pc (:point-collection @state)
                     x-series ((:get-x-series pc))
                     y-series ((:get-y-series pc))
                     w width
                     h height]
                 
                 (when-not (empty? x-series)
                   
                   ;; Drawing the curve
                   (when curve
                     (.beginPath ctx)
                     (.moveTo ctx (/ (first x-series) screen-ratio)
                             (/ (- h (first y-series)) screen-ratio))
                     
                     (let [spline (spline-constructor (clj->js x-series) (clj->js y-series))
                           interpolate (:interpolate spline)
                           x-interp (:x-series-interpolated @state)
                           y-interp (:y-series-interpolated @state)]
                       
                       (.fill x-interp 0)
                       (.fill y-interp 0)
                       
                       ;; Before the first point (if not at the left of the canvas)
                       (doseq [x (range (js/Math.ceil (first x-series)))]
                         (let [y (first y-series)]
                           (aset x-interp x (/ x w))
                           (aset y-interp x (/ y h))
                           (let [y-adj (cond (< y 0) 0.5
                                             (> y h) (- h 0.5)
                                             :else y)]
                             (.lineTo ctx (/ x screen-ratio) (/ (- h y-adj) screen-ratio)))))
                       
                       ;; Between the first and the last point
                       (doseq [x (range (js/Math.ceil (first x-series))
                                        (js/Math.ceil (last x-series)))]
                         (let [y (interpolate x)]
                           (aset x-interp x (/ x w))
                           (aset y-interp x (/ y h))
                           (let [y-adj (cond (< y 0) 0.5
                                             (> y h) (- h 0.5)
                                             :else y)]
                             (.lineTo ctx (/ x screen-ratio) (/ (- h y-adj) screen-ratio)))))
                       
                       ;; After the last point (if not at the right of the canvas)
                       (doseq [x (range (js/Math.ceil (last x-series)) w)]
                         (let [y (last y-series)]
                           (aset x-interp x (/ x w))
                           (aset y-interp x (/ y h))
                           (let [y-adj (cond (< y 0) 0.5
                                             (> y h) (- h 0.5)
                                             :else y)]
                             (.lineTo ctx (/ x screen-ratio) (/ (- h y-adj) screen-ratio))))))
                     
                     (let [curve-color (get-in @state [:options :curve-color])
                           color (if (= (:point-grabbed-index @state) -1)
                                   (:idle curve-color)
                                   (:moving curve-color))]
                       (set! (.-strokeStyle ctx) color)
                       (set! (.-lineWidth ctx) (/ (get-in @state [:options :curve-thickness]) screen-ratio))
                       (.stroke ctx)
                       (.closePath ctx)))
                   
                   ;; Drawing the control points
                   (when control
                     (doseq [i (range (count x-series))]
                       (.beginPath ctx)
                       (.arc ctx
                             (/ (nth x-series i) screen-ratio)
                             (/ (- h (nth y-series i)) screen-ratio)
                             (/ (get-in @state [:options :control-point-radius]) screen-ratio)
                             0
                             (* 2 js/Math.PI))
                       
                       (let [cp-color (get-in @state [:options :control-point-color])
                             hovered-idx (:point-hovered-index @state)
                             color (cond
                                     (= hovered-idx -1) (:idle cp-color)
                                     (= i hovered-idx)
                                     (if (:mouse-down @state)
                                       (:grabbed cp-color)
                                       (:hovered cp-color))
                                     :else (:idle cp-color))]
                         (set! (.-fillStyle ctx) color)
                         (.fill ctx)
                         (.closePath ctx)))))))
             
             ;; Draw the whole canvas
             (draw! []
               (.clearRect ctx 0 0 width height)
               (fill-background!)
               (draw-grid!)
               (draw-data!))
             
             ;; [EVENT] [PRIVATE]
             ;; The mouse moves over the canvas
             (on-mouse-move [evt]
               (let [pos (get-mouse-pos evt)
                     pc (:point-collection @state)
                     tolerance (get-in @state [:options :control-point-radius])]
                 (swap! state assoc :mouse pos)
                 
                 (if (:mouse-down @state)
                   ;; Mouse is down - dragging
                   (when-let [grabbed-idx (:point-grabbed-index @state)]
                     (when (>= grabbed-idx 0)
                       (let [new-idx ((:update-point pc) grabbed-idx (:x pos) (:y pos))]
                         (swap! state assoc :point-grabbed-index new-idx)
                         (draw!)
                         (when-let [callback (get-in @state [:on-events :move-point])]
                           (callback)))))
                   ;; Mouse is up - hovering
                   (let [hovered-idx ((:get-at pc) (:x pos) (:y pos) tolerance)]
                     (swap! state assoc :point-hovered-index (or hovered-idx -1))
                     (draw!)))))
             
             ;; [EVENT] [PRIVATE]
             ;; Mouse button is pressed down
             (on-mouse-down [evt]
               (swap! state assoc :mouse-down true)
               (let [hovered-idx (:point-hovered-index @state)]
                 (when (>= hovered-idx 0)
                   (swap! state assoc :point-grabbed-index hovered-idx)
                   (draw!))))
             
             ;; [EVENT] [PRIVATE]
             ;; Mouse button is released
             (on-mouse-up [evt]
               (swap! state assoc :mouse-down false)
               (let [grabbed-idx (:point-grabbed-index @state)]
                 (when (>= grabbed-idx 0)
                   (swap! state assoc :point-grabbed-index -1)
                   (draw!)
                   (when-let [callback (get-in @state [:on-events :release-point])]
                     (callback)))))
             
             ;; [EVENT] [PRIVATE]
             ;; Double click on the canvas
             (on-dbl-click [evt]
               (let [pos (get-mouse-pos evt)
                     pc (:point-collection @state)]
                 ((:add pc) pos)
                 (draw!)
                 (when-let [callback (get-in @state [:on-events :point-added])]
                   (callback))))
             
             ;; [EVENT] [PRIVATE]
             ;; Mouse enters the canvas
             (on-mouse-enter [evt]
               (set! (.. canvas -style -border) (get-in @state [:options :border-style :in])))
             
             ;; [EVENT] [PRIVATE]
             ;; Mouse leaves the canvas
             (on-mouse-leave [evt]
               (swap! state assoc :mouse nil)
               (set! (.. canvas -style -border) (get-in @state [:options :border-style :out])))
             
             ;; [EVENT] [PRIVATE]
             ;; A keyboard key is released
             (on-key-up [evt]
               ;; Mouse must be inside
               (when (:mouse @state)
                 (when (= (.-key evt) "d")
                   (let [pc (:point-collection @state)
                         hovered-idx (:point-hovered-index @state)]
                     (when ((:remove pc) hovered-idx)
                       (draw!)
                       (when-let [callback (get-in @state [:on-events :point-removed])]
                         (callback)))))))]
       
       ;; Set up event listeners
       (.addEventListener canvas "mousemove" on-mouse-move)
       (.addEventListener canvas "mousedown" on-mouse-down)
       (.addEventListener canvas "mouseup" on-mouse-up)
       (.addEventListener canvas "dblclick" on-dbl-click)
       (.addEventListener canvas "mouseenter" on-mouse-enter)
       (.addEventListener canvas "mouseleave" on-mouse-leave)
       (.addEventListener js/document "keyup" on-key-up)
       
       ;; Initial border style
       (set! (.. canvas -style -border) (get-in @state [:options :border-style :out]))
       
       ;; Return public API
       {:state state
        
        ;; Add a point to the collection
        ;; pt - of type {:x Number :y Number} and optionally the boolean properties :x-locked and :y-locked. x and y must be in [0, 1]
        :add
        (fn [pt & {:keys [draw] :or {draw true}}]
          (when (and (:x pt) (:y pt))
            (let [scaled-pt {:x (* (:x pt) width)
                             :y (* (:y pt) height)
                             :x-locked (:x-locked pt)
                             :y-locked (:y-locked pt)
                             :safe (:safe pt)}
                  pc (:point-collection @state)
                  idx ((:add pc) scaled-pt)]
              (when draw (draw!))
              (when-let [callback (get-in @state [:on-events :point-added])]
                (callback))
              idx)))
        
        ;; Remove a point using its index
        ;; index - index of the point to remove (from left to right, starting at 0)
        :remove
        (fn [idx]
          (let [pc (:point-collection @state)]
            (when ((:remove pc) idx)
              (draw!)
              (when-let [callback (get-in @state [:on-events :point-removed])]
                (callback)))))
        
        ;; Draw the whole canvas
        :draw draw!
        
        ;; Get a single interpolated value
        ;; x - normalized x (in [0, 1])
        ;; return - the normalized interpolated value
        :get-value
        (fn [x]
          (let [pc (:point-collection @state)
                x-series ((:get-x-series pc))
                y-series ((:get-y-series pc))
                w width
                h height]
            (cond
              ;; Before the first x, we return the first y
              (<= x (/ (first x-series) w))
              (/ (first y-series) h)
              
              ;; After the last x, we return the last y
              (>= x (/ (last x-series) w))
              (/ (last y-series) h)
              
              ;; Somewhere in the series, we interpolate
              :else
              (let [spline (spline-constructor (clj->js x-series) (clj->js y-series))
                    interpolate (:interpolate spline)]
                (/ (interpolate (* x w)) h)))))
        
        ;; Define an event
        ;; event-name - name of the event. "movePoint", "releasePoint", "pointAdded" or "pointRemoved"
        ;; callback - function to call when event occurs
        :on
        (fn [event-name callback]
          (swap! state assoc-in [:on-events (keyword event-name)] callback))}))))

;; =============================================================================
;; Exports
;; =============================================================================

;; Note: we chose not to export PointCollection

(def CubicSpline cubic-spline)
(def MonotonicCubicSpline monotonic-cubic-spline)
(def CanvasSpliner canvas-spliner)

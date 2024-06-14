package com.xiaohaoo.yolo.util

class DetectorUtils {
    companion object {

        private const val NUM_ELEMENTS = 8400
        private const val NUM_CHANNELS = 84
        private const val CONFIDENCE_THRESHOLD = 0.3F
        private const val IOU_THRESHOLD = 0.5F

        data class BoundingBox(
            val x1: Float,
            val y1: Float,
            val x2: Float,
            val y2: Float,
            val cx: Float,
            val cy: Float,
            val w: Float,
            val h: Float,
            val cnf: Float,
            val cls: Int,
        )

        fun boundingBox(array: FloatArray): List<BoundingBox>? {
            val boundingBoxes = mutableListOf<BoundingBox>()
            for (c in 0 until NUM_ELEMENTS) {
                var cnf = CONFIDENCE_THRESHOLD
                var maxIdx = -1
                var j = 4
                var arrayIdx = c + NUM_ELEMENTS * j
                while (j < NUM_CHANNELS) {
                    if (array[arrayIdx] > cnf) {
                        cnf = array[arrayIdx]
                        maxIdx = j - 4
                    }
                    j++
                    arrayIdx += NUM_ELEMENTS
                }
                if (cnf > CONFIDENCE_THRESHOLD) {
                    val cx = array[c] // 0
                    val cy = array[c + NUM_ELEMENTS] // 1
                    val w = array[c + NUM_ELEMENTS * 2]
                    val h = array[c + NUM_ELEMENTS * 3]
                    val x1 = cx - (w / 2F)
                    val y1 = cy - (h / 2F)
                    val x2 = cx + (w / 2F)
                    val y2 = cy + (h / 2F)
                    if (x1 < 0F || x1 > 1F) continue
                    if (y1 < 0F || y1 > 1F) continue
                    if (x2 < 0F || x2 > 1F) continue
                    if (y2 < 0F || y2 > 1F) continue
                    boundingBoxes.add(BoundingBox(x1 = x1, y1 = y1, x2 = x2, y2 = y2, cx = cx, cy = cy, w = w, h = h, cnf = cnf, cls = maxIdx))
                }
            }
            if (boundingBoxes.isEmpty()) return null
            val sortedBoxes = boundingBoxes.sortedByDescending { it.cnf }.toMutableList()
            val selectedBoxes = mutableListOf<BoundingBox>()
            while (sortedBoxes.isNotEmpty()) {
                val box1 = sortedBoxes.first()
                selectedBoxes.add(box1)
                sortedBoxes.remove(box1)
                val iterator = sortedBoxes.iterator()
                while (iterator.hasNext()) {
                    val box2 = iterator.next()
                    val x1 = maxOf(box1.x1, box2.x1)
                    val y1 = maxOf(box1.y1, box2.y1)
                    val x2 = minOf(box1.x2, box2.x2)
                    val y2 = minOf(box1.y2, box2.y2)
                    val intersectionArea = maxOf(0F, x2 - x1) * maxOf(0F, y2 - y1)
                    val box1Area = box1.w * box1.h
                    val box2Area = box2.w * box2.h
                    val iou = intersectionArea / (box1Area + box2Area - intersectionArea)
                    if (iou >= IOU_THRESHOLD) {
                        iterator.remove()
                    }
                }
            }
            return selectedBoxes
        }
    }
}
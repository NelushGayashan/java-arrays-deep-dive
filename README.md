# Java Arrays — Deep Dive

Companion Maven project for the Medium article  
**"Arrays in Java: The Complete Foundation Every Developer Must Master"**  
by [Nelush Gayashan Fernando](https://github.com/NelushGayashan)

---

## Quick Start

```bash
# Run all section demos (matches article order)
mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.ArrayRunner"

# Run a single section
mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.patterns.TwoPointerTechnique"

# Run all tests (65 test cases)
mvn test
```

---

## Project Structure

```
src/main/java/com/nelush/arrays/
├── ArrayRunner.java                        ← run everything from here
├── basics/
│   ├── ArrayMemoryModel.java               ← §1 contiguous memory, O(1) formula, covariance
│   ├── ArraysUtility.java                  ← §2 sort, binarySearch, copyOf, fill, equals, stream
│   └── CacheLocality.java                  ← §3 array vs LinkedList benchmark, row vs col major
├── dynamicarray/
│   ├── DynamicArrayInternals.java          ← §4 ArrayList resize, pre-alloc, complexity
│   └── CustomDynamicArray.java             ← §4 from-scratch generic dynamic array
├── patterns/
│   ├── TwoPointerTechnique.java            ← §5 reverse, two-sum, three-sum, water, merge
│   ├── SlidingWindow.java                  ← §6 fixed/variable window, monotonic deque
│   └── PrefixSum.java                      ← §7 1D, 2D, subarray count, product except self
├── algorithms/
│   ├── KadaneAlgorithm.java               ← §8 max subarray, circular, product variants
│   ├── BinarySearch.java                  ← §9 classic, first/last, rotated, answer-domain
│   ├── DutchNationalFlag.java             ← §10 sort 0/1/2, three-way partition
│   └── SortingAlgorithms.java             ← §11 bubble, insertion, merge, quick, counting
└── advanced/
    └── MatrixOperations.java              ← §12 rotate, spiral, set-zeroes, multiply

src/test/java/com/nelush/arrays/
└── ArraysTest.java                        ← 65 JUnit 5 tests covering all sections
```

---

## What Each Section Covers

| Section | Class | Article Topic |
|---------|-------|---------------|
| §1 | `ArrayMemoryModel` | Address formula `base + i×size`, primitive vs object, covariance |
| §2 | `ArraysUtility` | sort (Dual-Pivot QS / TimSort), binarySearch, copyOf, fill, equals, stream |
| §3 | `CacheLocality` | Cache lines, 10–50x array vs LinkedList, row-major vs column-major |
| §4 | `DynamicArrayInternals`, `CustomDynamicArray` | 1.5x resize factor, amortized O(1), pre-allocation |
| §5 | `TwoPointerTechnique` | Reverse, two-sum, remove dups, water, three-sum |
| §6 | `SlidingWindow` | Fixed window, variable window, monotonic deque max |
| §7 | `PrefixSum` | O(1) range query, 2D sum, subarray count k, product except self |
| §8 | `KadaneAlgorithm` | Max subarray, circular variant, max product |
| §9 | `BinarySearch` | Classic, first/last occurrence, rotated array, answer-domain |
| §10 | `DutchNationalFlag` | Sort 0/1/2 in O(n) O(1), three-way partition |
| §11 | `SortingAlgorithms` | Bubble, insertion, merge, quick, counting |
| §12 | `MatrixOperations` | Rotate 90°, spiral, set zeroes, multiply |

---

## Complexity Reference

### Core Operations
| Operation | Array (primitive) | ArrayList |
|---|---|---|
| Access by index | O(1) | O(1) |
| Search (unsorted) | O(n) | O(n) |
| Search (sorted, binary) | O(log n) | O(log n) |
| Insert at tail | — | O(1) amortized |
| Insert at index i | — | O(n) |
| Delete at index i | — | O(n) |

### Algorithmic Patterns
| Pattern | Time | Space |
|---|---|---|
| Two-pointer | O(n) | O(1) |
| Sliding window | O(n) | O(1)–O(k) |
| Prefix sum build | O(n) | O(n) |
| Prefix sum query | O(1) | — |
| Kadane's algorithm | O(n) | O(1) |
| Binary search | O(log n) | O(1) |
| Dutch National Flag | O(n) | O(1) |
| Merge sort | O(n log n) | O(n) |
| Quick sort (avg) | O(n log n) | O(log n) |

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.8+ |

---

## Author

**Nelush Gayashan Fernando** — Lead Software Engineer  
[GitHub](https://github.com/NelushGayashan) · [Medium](https://medium.com/@NelushGayashan)

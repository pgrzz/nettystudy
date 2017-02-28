package Leet;

import scala.Int;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by lenovo on 2016/10/13.
 */
public class Solution {

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode(int x) { val = x; }
     * }
     */
    public ListNode reverseList(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode first = null;
        ListNode second = head;
        ListNode third = null;
        while (second != null) {  // 思路 就是 1：先用下一个暂存指针存下当前节点的下一个节点，2：让下一个节点指向前一个节点。
            third = second.next; // 3： 因为前一个节点已经被指了 它向前走 到当前节点的位置
            second.next = first;
            first = second;
            second = third;        // 4 让 当前指针又向前走 走到下一个 要交换的节点
        }
        return first;
    }


    public ListNode oddEvenList(ListNode head) {


        if (head == null || head.next == null || head.next.next == null) {
            return head;
        }
        int status = 1;
        ListNode odd = new ListNode(head.val);
        ListNode even = new ListNode(head.next.val);
        ListNode first = odd;
        ListNode seconrd = even;
        ListNode ttNode = head;
        for (; ; ) {
            if (ttNode == null) {
                break;
            }
            if (status == 1) {
                odd.next = new ListNode(ttNode.val);
                odd = odd.next;
                status = 0;
            } else {
                even.next = new ListNode(ttNode.val);
                even = even.next;
                status = 1;
            }
            ttNode = ttNode.next;
        }
        ListNode temp = first;
        while (true) {
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = seconrd.next;
            break;
        }
        return first.next;
    }

    /**
     * 利用下一个节点的前提是 能够保证下一个节点不为空
     *
     * @param node
     */
    public void deleteNode(ListNode node) {

        node.val = node.next.val;
        node.next = node.next.next;

    }

    /**
     * 利用快 慢 指针定位 节点位置
     *
     * @param head
     * @return
     */
    private ListNode partition(ListNode head) { // 利用 节点差值可以得到中间的节点 p.next , p.next.next


        ListNode p = head;
        while (p.next != null && p.next.next != null) {
            p = p.next.next;
            head = head.next;
        }
        p = head.next;
        head.next = null;
        return p;

    }
    private ListNode partition(ListNode head,ListNode tail){

        ListNode p = head;
        while (p.next != tail && p.next.next != tail) {
            p = p.next.next;
            head = head.next;
        }
        p = head.next;
        head.next = null;
        return p;
    }

    /**
     * 先找到中间点 ->翻转-> 比较
     *
     * @param head
     * @return
     */
    public boolean isPalindrome(ListNode head) {

        if (head == null || head.next == null) return true;

        ListNode middle = partition(head);
        middle = reverseList(middle);

        while (head != null && middle != null) {
            if (head.val != middle.val) {
                return false;
            }
            head = head.next;
            middle = middle.next;
        }
        return true;

    }

    /**
     * 删除给定的值
     * 利用 一个虚拟头 便于 迭代头节点
     * 思路 虚拟节点记录 迭代节点的状态 当 迭代节点不是要删除节点 那么 虚拟节点的下一个就是真实节点 否则就指向删除节点的下一个节点。
     *
     * @param head
     * @param val
     * @return
     */
    public ListNode removeElements(ListNode head, int val) {

        ListNode fristNode = new ListNode(0);
        fristNode.next = head;
        ListNode nowhead = fristNode;
        ListNode cur = head;
        while (cur != null) {
            if (cur.val == val) {  // 思路当前节点如果不是要删除的节点就说明是节点头 如果要删除了就让虚拟当前节点头指向下一个节点头
                nowhead.next = cur.next;
            } else {
                nowhead = nowhead.next;
            }
            cur = cur.next;
        }
        return fristNode.next;

    }

    /**
     * 翻转 思路 先找到 断链的 前一个节点 和 后一个 节点 然后 让 前一个节点的值改变为 翻转后的新节点 再让翻转的tail节点 的下一个是 断链后节点。
     *
     * @param head
     * @param m
     * @param n
     * @return
     */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || head.next == null || m == n) {
            return head;
        }
        ListNode start = new ListNode(-1); //利用虚拟指针使的能够遍历到头指针方便
        start.next = head;

        ListNode prv = start;
        ListNode next = start;

        int count = 0;
        while (count < n) {
            if (next == null) {
                return start.next;
            }
            if (count < m - 1)
                prv = prv.next;
            next = next.next;
            count++;
        }

        ListNode second = prv.next;     //这里很关键 保存 头结点 和尾节点
        ListNode temp = next.next;
        next.next = null;       // 断链
        prv.next = reverseList(second);
        second.next = temp;      //接链
        return start.next;

    }


    /**
     * 找到相同的插入点  第一步是消除 两个链表的步数差
     * 设 A 长 a  B  b  b>a
     * 则有 当a 走完时让 a走 b ，b 走 a   为  b   b为 b-a+a
     * 两个长度一样的地方开始 如果有相同的节点则会相等 否则就都走到null
     *
     * @param headA
     * @param headB
     * @return
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {


        if (headA == null || headB == null) {
            return null;
        }
        ListNode t1 = headA, t2 = headB;
        while (t1 != t2) {
            t1 = t1 == null ? headB : t1.next;
            t2 = t2 == null ? headA : t2.next;
        }

        return t1;
    }

    /**
     * 单链表的归并排序 ，找到 每次 分隔的中间 然后 锻炼  递归 marge
     *
     * @param head
     * @return
     */
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode slow = head;
        ListNode fast = head;
        for (; fast.next != null && fast.next.next != null; fast = fast.next.next, slow = slow.next) ;
        ListNode middle = slow.next;
        slow.next = null;
        ListNode t1 = sortList(head);
        ListNode t2 = sortList(middle);
        //marge 历程

        return marge(t1, t2);
    }

    /**
     * 插入排序 排单链表
     *
     * @param head
     * @return
     */
    public ListNode insertionSortList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode start = new ListNode(-1);
        ListNode pre = start;
        ListNode current = head;
        while (current != null) {
            pre = start;
            while (pre.next != null && pre.next.val < current.val) {
                pre = pre.next;
            }
            ListNode next = current.next;
            current.next = pre.next;
            pre.next = current;
            current = next;
        }
        return start.next;
    }


    /**
     * 单链表 的 找中值 翻转 插入
     * Given a singly linked list L: L0→L1→…→Ln-1→Ln,
     * reorder it to: L0→Ln→L1→Ln-1→L2→Ln-2→…
     *
     * @param head
     */
    public static void reorderList(ListNode head) {

        if (head == null || head.next == null) {
            return;
        }

        ListNode slow = head, fast = head;
        ListNode middle;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        middle = slow.next;
        slow.next = null;

        //reveseList

        ListNode first = null;
        ListNode second = middle;
        ListNode third = null;
        if (middle != null || middle.next != null) {
            while (second != null) {
                third = second.next;
                second.next = first;
                first = second;
                second = third;
            }
        }
        //marge
        ListNode n1 = head;
        ListNode n2 = first;

        while (n1 != null && n2 != null) {
            ListNode t1 = n1.next;
            ListNode t2 = n2.next;
            n1.next = n2;
            if (t1 == null)
                break;
            n2.next = t1;
            n1 = t1;
            n2 = t2;

        }

    }

    /**
     * 快慢指针 当 快慢指针 同步的时候就说明 存在环
     *
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null)
            return false;
        ListNode t1 = head;
        ListNode t2 = head;
        while (t2.next != null && t2.next.next != null) {
            t1 = t1.next;
            t2 = t2.next.next;
            if (t1 == t2)
                return true;

        }

        return false;
    }

    /**
     * 节点 当 head ==t2 就是 环点
     *
     * @param head
     * @return
     */
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null)
            return null;
        ListNode t1 = head;
        ListNode t2 = head;
        while (t2.next != null && t2.next.next != null) {
            t1 = t1.next;
            t2 = t2.next.next;
            if (t1 == t2) {
                while (head != t2) {
                    t2 = t2.next;
                    head = head.next;
                }
                return head;
            }

        }
        return null;
    }

    /**
     * 正常的思路解就可以
     * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
     * Output: 7 -> 0 -> 8
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        ListNode t1 = l1; // 3 4 2     8 1
        ListNode t2 = l2; // 4 6 5     0
        ListNode listNode = new ListNode(-1);
        ListNode rr = listNode;
        int jinwei = 0;
        while (t1 != null || t2 != null) {
            int temp1 = 0, temp2 = 0;
            int shenxiade = 0;
            if (t1 != null) {
                temp1 = t1.val; // 3   8
            }
            if (t2 != null) {
                temp2 = t2.val; // 4   0
            }
            shenxiade = (temp1 + temp2 + jinwei) % 10;
            ListNode listNode1 = new ListNode(shenxiade);
            rr.next = listNode1;            // 7  8
            rr = rr.next;
            jinwei = ((temp1 + temp2) + jinwei) / 10;
            if (t1 != null) {
                t1 = t1.next;
            }
            if (t2 != null) {
                t2 = t2.next;
            }
            rr.next = jinwei == 0 ? null : new ListNode(jinwei);
        }
        return listNode.next;
    }

    /**
     * 这题其实和上面的思虑一样 当长度不一样的时候 1 用0 来代替这次相加  进位的长度为 ((temp1 + temp2) + jinwei) / 10;
     * 区别 ： 这道题给出了一个条件 出现的字符只会是 '0' - '9'    说明可以利用字符的ACILL 码来 得到 char-char 差值就是 int 值
     *
     * @param num1
     * @param num2
     * @return
     */
    public String addStrings(String num1, String num2) {
        StringBuilder result = new StringBuilder("");
        int carray = 0;
        for (int i = num1.length() - 1, j = num2.length() - 1; i >= 0 || j >= 0 || carray == 1; i--, j--) {
            int x = i < 0 ? 0 : num1.charAt(i) - '0';
            int y = j < 0 ? 0 : num2.charAt(j) - '0';
            result.append((x + y + carray) % 10);
            carray = (x + y + carray) / 10;
        }
        return result.reverse().toString();
    }


    /**
     * 删除 倒数 n 个节点
     * 先翻转 然后 删除 再翻转
     *
     * @param head
     * @param n
     * @return
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {

        head = reverseList(head);

        if (head == null) {
            return head;
        }

        ListNode start = new ListNode(-1);
        start.next = head;
        ListNode Vircur = start;
        ListNode cur = head;
        for (int i = 1; i < n; i++) {
            cur = cur.next;
            Vircur = Vircur.next;
        }
        if (cur != null) {
            Vircur.next = cur.next;
        } else {
            Vircur.next = null;
        }
        return reverseList(start.next);
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(-1);
        ListNode p = result;
        for (; l1 != null && l2 != null; p = p.next) {
            if (l1.val < l2.val) {
                p.next = l1;
                l1 = l1.next;
            } else {
                p.next = l2;
                l2 = l2.next;
            }
        }
        if (l1 != null) {
            p.next = l1;
        }
        if (l2 != null) {
            p.next = l2;
        }
        return result.next;
    }


    /**
     * 归并排序 marge 先比较 加入一个中间数组 然后 copy 剩下的
     *
     * @param t1
     * @param t2
     * @return
     */
    private ListNode marge(ListNode t1, ListNode t2) {

        ListNode result = new ListNode(-1);
        ListNode p = result;
        for (; t1 != null && t2 != null; p = p.next) {
            int val = t1.val;
            if (val < t2.val) {
                p.next = t1;
                t1 = t1.next;
            } else {
                p.next = t2;
                t2 = t2.next;
            }

        }
        //cope last
        while (t1 != null) {
            p.next = t1;
            p = p.next;
            t1 = t1.next;
        }
        while (t2 != null) {
            p.next = t2;
            p = p.next;
            t2 = t2.next;
        }
        return result.next;

    }

    /**
     * Given 1->2->3->4, you should return the list as 2->1->4->3.
     *
     * @param head
     * @return
     */
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode current = dummy;
        while (current.next != null && current.next.next != null) {
            ListNode first = current.next;
            ListNode second = current.next.next;
            first.next = second.next;
            current.next = second;
            current.next.next = first;
            current = current.next.next;
        }
        return dummy.next;
    }

    public int thirdMax(int[] nums) {

        int one = Integer.MIN_VALUE, two = Integer.MIN_VALUE, three = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int temp = nums[i];
            one = temp >= one ? temp : one;
            if (one == temp)
                two = one >= two ? temp : two;
            if (two == temp)
                three = temp >= three ? temp : three;
        }
        three = three == Integer.MIN_VALUE ? two = two == Integer.MIN_VALUE ? one : two : three;
        return three;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

    }

    //斐波那契数列

    public int fib(int n) {

        if (n <= 1)
            return 1;
        else
            return fib(n - 1) + fib(n - 2);

    }

    public int fibDp(int n) {
        if (n < 1)
            return 1;
        int slow = 1;
        int fast = 1;
        int answer = 1;
        for (int i = 2; i <= n; i++) {
            answer = slow + fast;
            fast = slow;
            slow = answer;
        }
        return answer;

    }

    /**
     * 这道题的思想就是marge 要注意的一个点是
     * double prv=result[result.length/2-1];
     * double next=result[result.length/2];
     * double t=(prv+next)/2;
     * 对于 数组的中位数的前一个后一个
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length == 0 && nums2.length == 0)
            return 0;
        int n = nums1.length;
        int m = nums2.length;
        int[] result = new int[n + m];
        int i = 0;
        int j = 0;
        for (int t = 0; t < result.length; t++) {
            if (i < n && j < m) {
                if (nums1[i] < nums2[j]) {
                    result[t] = nums1[i];
                    i++;
                } else {
                    result[t] = nums2[j];
                    j++;
                }
            } else {
                //copy last
                if (i < n) {
                    result[t] = nums1[i];
                    i++;
                }
                if (j < m) {
                    result[t] = nums2[j];
                    j++;
                }
            }
        }
        if (result.length % 2 != 0) {
            // 中数
            return result[result.length / 2];
        } else {
            double prv = result[result.length / 2 - 1];
            double next = result[result.length / 2];
            double t = (prv + next) / 2;
            return t;
        }
    }


    // 只要对边界做判定就可以说
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap();

        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                if ((i - map.get(nums[i])) <= k) {
                    return true;
                } else {
                    map.put(nums[i], i);
                }
            } else {
                map.put(nums[i], i);
            }
        }
        return false;
    }


    /**
     * For example, given array S = [-1, 0, 1, 2, -1, -4],
     * 思路 从 每个数的 begin 到 end 的区间 开始 收敛 至 begin==end
     * A solution set is:
     * [
     * [-1, 0, 1],
     * [-1, -1, 2]
     * ]
     * 一开从数的中间开始寻找这样子会 -1 -1  2 这种情况  考虑不到会把 -1 连续跳过.
     * 现在 从   i    i+1 到 结束的 区间开始 寻找 那么  -1  i -1 i+1=begin    2   end  这样子就能够保证取到所有的情况.
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i - 1] == nums[i]) continue;
            int begin = i + 1;
            int end = nums.length - 1;
            while (begin < end) {
                int sum = nums[begin] + nums[i] + nums[end];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[begin], nums[i], nums[end]));
                    while (begin < end && nums[begin] == nums[begin + 1]) begin++;
                    while (end > begin && nums[end] == nums[end - 1]) end--;
                    begin++;
                    end--;
                } else if (begin < end && sum < 0) {
                    begin++;
                } else if (begin < end && sum > 0) {
                    end--;
                }
            }
        }
        return result;

    }

    public int[] twoSum(int[] numbers, int target) {

        HashMap<Integer, Integer> map = new HashMap();
        int[] result = null;
        for (int i = 0; i < numbers.length; i++) {
            int temp = numbers[i];
            if (map.containsKey(temp)) {
                // 说明存在这个差值
                result = new int[]{map.get(temp) + 1, i + 1};
            } else {
                map.put(target - temp, i);
            }

        }
        return result;
    }


    /**
     * 4 5   5     o  0  e 4
     *
     * @param nums
     * @param val
     * @return
     */
    public int removeElement(int[] nums, int val) {
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[index++] = nums[i];
            }
        }
        return index;

    }

    public int removeDuplicates(int[] nums) { // 1, 0 ,1
        int index = 1;
        for (int j = 0; j < nums.length - 1; j++) {
            if (nums[j] != nums[j + 1])
                nums[index++] = nums[j + 1];
        }
        return index;
    }


    /**
     * 1 个记录 0 到哪里了
     *
     * @param nums
     */
    public void moveZeroes(int[] nums) {
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[index++] = nums[i];
            }
        }
        for (int i = index; i < nums.length; i++) {
            nums[i] = 0;
        }


    }


    /**
     * @param nums
     * @param target
     * @return
     */
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int sum = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            int begin = i + 1;
            int end = nums.length - 1;
            if (i > 0 && nums[i - 1] == nums[i]) continue;
            while (begin < end) {
                int tt = nums[begin] + nums[i] + nums[end];
                if (Math.abs(sum - target) > Math.abs(tt - target)) {   // 原来的大于现在的
                    sum = tt;
                }
                if (tt == target)
                    return target; //最优
                else if (tt > target)
                    end--;
                else
                    begin++;

            }
        }
        return sum;
    }


    public  void fastSort(int[] a, int begin, int end) {

        if (begin < end) {
            int middle = partion(a, begin, end);
            fastSort(a, begin, middle-1 );
            fastSort(a, middle + 1, end);
        }

    }


    private int partion(int[] numbers, int begin, int end) {
        int value = numbers[end];
        int index = begin - 1;

        for (int j = begin; j < end; j++){
            if (numbers[j] <= value){
                index++;
                exchange(numbers, index, j);
            }
        }
        exchange(numbers, index + 1, end);

        return index+1;
    }

//    private void exchange(int[] a, int first, int second) {
//        int temp = a[second];
//        a[second] = a[first];
//        a[first] = temp;
//    }

    private int getMiddle(int[] a, int left, int right) {

        int mid = (left + right) / 2;
        if (a[left]-(a[right]) < 0) {
            if (a[right]-(a[mid]) < 0)
                return right;
            else if (a[left]-(a[mid]) < 0)
                return mid;
            else
                return left;
        } else {
            if (a[left]-(a[mid]) < 0)
                return left;
            else if (a[right]-(a[mid]) > 0)
                return mid;
            else
                return right;
        }

    }


    /**
     * [1,0]
     * 1
     * [2]
     * 1
     * Output:
     * [2,2]
     * Expected:
     * [1,2]
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int len = m + n;
        int frist = 0;
        int second = 0;
        int[] result = new int[m + n];
        for (int i = 0; i < len; i++) {
            if (frist < m && second < n) {
                if (nums1[frist] < nums2[second])
                    result[i] = nums1[frist++];
                else
                    result[i] = nums2[second++];
            } else {
                //copy last
                if (second < n)
                    result[i] = nums2[second++];
                else
                    result[i] = nums1[frist++];
            }
        }
        for (int i = 0; i < result.length; i++) {
            nums1[i] = result[i];
        }
    }

    public String frequencySort(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char t = s.charAt(i);
            if (map.containsKey(t)) {
                map.put(t, map.get(t) + 1);
            } else {
                map.put(t, 1);
            }
        }
        List<String> temp = new ArrayList<>();
        for (Character character : map.keySet()) {
            Integer a = map.get(character);
            StringBuilder stringBuilder = new StringBuilder("");
            for (int j = 0; j < a; j++) {
                stringBuilder.append(character);
            }
            temp.add(stringBuilder.toString());
        }
        temp.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() < o2.length())
                    return 1;
                else if (o1.length() == o2.length())
                    return 0;
                else
                    return -1;
            }
        });
        String result = "";
        for (String s1 : temp) {
            result += s1;
        }
        return result;
    }

    public String reverseString(String s) {

        if (s != null && s.equals("")) {
            return "";
        }
        char[] a = s.toCharArray();
        int begin = 0;
        int end = a.length - 1;
        while (begin < end) {
            char temp = a[begin];
            a[begin] = a[end];
            a[end] = temp;
            begin++;
            end--;
        }
        return new String(a);

//        String t=new StringBuilder(s).reverse().toString();
//       return t;
    }

    public String reverseVowels(String s) {
        if (s != null && s.equals("")) {
            return "";
        }
        char[] a = s.toCharArray();
        char[] alphabet = new char[256];
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = 0;
        }
        alphabet['a'] = 1;
        alphabet['o'] = 1;
        alphabet['e'] = 1;
        alphabet['i'] = 1;
        alphabet['u'] = 1;
        alphabet['A'] = 1;
        alphabet['O'] = 1;
        alphabet['E'] = 1;
        alphabet['I'] = 1;
        alphabet['U'] = 1;
        int begin = 0;
        int end = s.length() - 1;
        while (begin < end) {
            char beginC = a[begin];
            char endC = a[end];
            if (alphabet[beginC] == 1 && alphabet[endC] == 1) {
                char temp = a[begin];
                a[begin] = a[end];
                a[end] = temp;
                begin++;
                end--;
            } else {
                if (alphabet[beginC] != 1)
                    begin++;
                if (alphabet[endC] != 1)
                    end--;
            }

        }
        return new String(a);
    }


    public ListNode partition(ListNode head, int x) {
        if (head == null || head.next == null)
            return head;
        ListNode begin = new ListNode(-1);
        ListNode t1 = begin;
        ListNode end = new ListNode(-1);
        ListNode t2 = end;
        ListNode ptr = head;
        while (ptr != null) {
            if (ptr.val < x) {
                t1.next = ptr;
                t1 = t1.next;
                ptr = ptr.next;
                t1.next = null; // 经常忘记断链
            } else {
                t2.next = ptr;
                t2 = t2.next;
                ptr = ptr.next;
                t2.next = null;
            }
        }
        t1.next = end.next;
        return begin.next;
    }


    public String minWindow(String s, String t) {

        //   Char

        return "";
    }

    public boolean isPalindrome(String s) {
        s = s.toLowerCase();
        char[] a = s.toCharArray();
        int begin = 0;
        int end = s.length() - 1;
        while (begin < end) {
            if (!Character.isAlphabetic(a[begin]) && !Character.isDigit(a[begin])) {
                begin++;
                continue;
            }
            if (!Character.isAlphabetic(a[end]) && !Character.isDigit(a[end])) {
                end--;
                continue;
            }
            if (a[begin] != a[end]) {
                return false;
            }
            begin++;
            end--;
        }
        return true;
    }




    /**
     * Or type the positive root of
     * x * x + -2 * x - 8 = 0:
     * x-1
     * Given an array with n objects colored red, white or blue, sort them so that objects of the same color are adjacent, with the colors in the order red, white and blue.
     * Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.
     *
     * @param nums
     */
    public void sortColors(int[] nums) {

    }

    /**
     * 1 0
     *
     * @param coins
     * @param amount
     * @return
     */
    public int coinChange(int[] coins, int amount) {

        if (amount == 0) return 0;
        int[] amounts = new int[amount + 1];
        for (int i = 0; i < amounts.length; i++) {
            amounts[i] = Integer.MAX_VALUE;
        }
        amounts[0] = 0;
        for (int i = 1; i < amounts.length; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (i >= coins[j] && amounts[i - coins[j]] != Integer.MAX_VALUE) {
                    amounts[i] = Math.min(amounts[i], amounts[i - coins[j]] + 1);
                }
            }
        }
        if (amounts[amount] != Integer.MAX_VALUE) {
            return amounts[amount];
        } else {
            return -1;
        }
    }


    /**
     * Find the contiguous subarray within an array (containing at least one number) which has the largest sum.
     * 0,1
     * For example, given the array [-2,1,-3,4,-1,2,1,-5,4],
     * <p>
     * the contiguous subarray [4,-1,2,1] has the largest sum = 6.
     *
     * @param nums
     * @return
     */
    public int maxSubArray(int[] nums) {
       int len=nums[0];
       int sum=0;
        for(int i=0;i<nums.length;i++){
            sum=nums[i]+sum;
            len=Math.max(sum,len);
            if(sum<0) sum=0;
        }
        return len;
    }


    /**
     * 5，3，4，8，6，7
     根据上面找到的状态，我们可以得到：（下文的最长非降子序列都用LIS表示）

     前1个数的LIS长度d(1)=1(序列：5)
     前2个数的LIS长度d(2)=1(序列：3；3前面没有比3小的)
     前3个数的LIS长度d(3)=2(序列：3，4；4前面有个比它小的3，所以d(3)=d(2)+1)
     前4个数的LIS长度d(4)=3(序列：3，4，8；8前面比它小的有3个数，所以 d(4)=max{d(1),d(2),d(3)}+1=3)
     OK，分析到这，我觉得状态转移方程已经很明显了，如果我们已经求出了d(1)到d(i-1)， 那么d(i)可以用下面的状态转移方程得到：

     d(i) = max{1, d(j)+1},其中j<i,A[j]<=A[i]
     用大白话解释就是，想要求d(i)，就把i前面的各个子序列中，
     最后一个数不大于A[i]的序列长度加1，然后取出最大的长度即为d(i)。
     当然了，有可能i前面的各个子序列中最后一个数都大于A[i]，那么d(i)=1， 即它自身成为一个长度为1的子序列。
     * @param nums
     * @return
     */
    public int lengthOfLIS(int[] nums) {
        if(nums.length==0)
            return 0;
        int dp[]=new int[nums.length];
        int max=1;
        for(int i=0;i<nums.length;i++){
            dp[i]=1;
            for(int j=0;j<i;j++){
                if(nums[j]<nums[i]){ // 满足 递增 和前面的最优状态
                    dp[i]=Math.max(dp[i],dp[j]+1);
                    if(dp[i]>max)
                        max=dp[i];
                }
            }
        }
    return max;
    }

    /**
     *       d(0)=0
     *       d(1)=d(0)+1
     *       d(2)=d(2-1)+1 d(2-2)+1
     *       d(3)=d(3-1)+1 d(3-2)+1
     *       d(4)=d(4-1)+1 d(4-2)+1
     * @param n
     * @return
     */
    public int climbStairs(int n) {
        if(n==0)return 0;
        int dp[]=new int[n+1];
        dp[0]=1;
        for(int i=1;i<dp.length;i++){
           dp[i]+=dp[i-1];
            if(i>=2)
             dp[i]+=dp[i-2];
        }
        return dp[n];
    }




    /**
     * 二维上的  dp[m][n]的状态由 dp[m-1][n] + dp[m][n-1]
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths(int m, int n) {
        if(m<1 || n<1)return 0;
//            int [][]dp=new int[m+1][n+1];
//        for(int i=0;i<m+1;i++){
//            dp[i][1]=1;
//        }
//        for(int j=0;j<n+1;j++){
//            dp[1][j]=1;
//        }
//      for(int i=2;i<m+1;i++){
//        for(int j=2;j<n+1;j++){
//            dp[i][j]=dp[i-1][j]+dp[i][j-1];
//        }
//      }
//        return dp[m][n];
       int dp[]=new int[n];         //思想 是 我这一层只要上一层的就够了所以每次就保存上一层就好了
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(i==0 || j==0){   // 第一次的值则是  1+0 则  i=j=dp[1][1]=1;
                    dp[j]=1;        // 当 i<1 是 dp[0] 这个时候存储的就是 模拟的 dp[i][1]的边界情况 当 =1 时就利用上次的 保存在 dp[j]中的则就是dp[i-1][j]的值
                }else{
                    dp[j]=dp[j]+dp[j-1];
                }
            }
        }
        return dp[n-1];

    }




    /**找到从下而上的最小值 只要保存每一层的min dp 状态就好了
     * 自底向上
     * step[j] =Math.min(step[j],step[j+1])+temp.get(j);
     *
     * @param triangle
     * @return
     */
    public int minimumTotal(List<List<Integer>> triangle) {
        if(triangle==null || triangle.size()==0)
            return 0;
        int n=triangle.size();
      int []step=new int[n];
        List<Integer> init=triangle.get(n-1);
        for(int i=0;i<step.length;i++){
            step[i]=init.get(i);
        }
            if(n>1) {
                for (int i = n - 2; i >= 0; i--) {
                    List<Integer> temp = triangle.get(i);
                    int m = temp.size();
                    for (int j = 0; j < m; j++) {
                        step[j] =Math.min(step[j],step[j+1])+temp.get(j);
                    }
                }
            }
            return step[0];
    }


    /**
     *  二维上 还是 层数滚动 当碰到障碍时就把这条路径置为0就可以了 不然就是dp[j]+dp[j-1]
     *    if(j<1)
     dp[j]=obstacleGrid[i][j]==1?0:dp[j];
     else
     dp[j]=obstacleGrid[i][j]==1?0:dp[j]+dp[j-1];
     * @param obstacleGrid
     * @return
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {

        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        if (obstacleGrid == null || obstacleGrid.length == 0 || obstacleGrid[0][0] == 1)   return 0;
        int dp[] = new int[n];
        dp[0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if(j<1)
                 dp[j]=obstacleGrid[i][j]==1?0:dp[j];
                else
                  dp[j]=obstacleGrid[i][j]==1?0:dp[j]+dp[j-1];
            }
        }
        return  dp[n - 1];
    }



    /**
        前一个状态和前 n-2个状态
     dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
     * @param nums
     * @return
     */
    public int rob(int[] nums) {
        if(nums.length<1)
            return 0;
        int dp[]=new int[nums.length];
        if(nums.length>=2) {
            dp[0]=nums[0];
            dp[1]=Math.max(nums[0],nums[1]);
            for (int i = 2; i < nums.length; i++) {
                dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
            }
        }else{
            dp[nums.length-1]=nums[0];
        }
        return dp[nums.length-1];
    }



    public ListNode deleteDuplicates(ListNode head) {
     if(head==null)return head;
        ListNode start=new ListNode(-1);
        start.next=head;
        ListNode first=start;
        ListNode second=head;
       while(second!=null){
           while(second.next!=null && second.val==second.next.val) {
              second=second.next;
           }
           if(first.next==second){
               first=first.next;
           }else{
               first.next=second.next;
           }
           second=second.next;
       }
        return start.next;
    }


    /**
     * Given a m x n grid filled with non-negative numbers,
     * find a path from top left to bottom right which minimizes the sum of all numbers along its path.
     * 初始状态 每一行的最左边的那一个数是只能够由 上一层得到的 所以就有 dp[0]=dp[0]+grid[i][0];
     * 对于每一个数字 就是从 左边或者上边来得到状态
     * 这个的dp 巧妙的地方在于 对于每一个数 它的dp[j-1]保存的是它左边的状态dp[j]保存的上面的状态
     * 所以状态转移   dp[j]=Math.min(dp[j],dp[j-1])+grid[i][j];
     * @param grid
     * @return
     */
    public int minPathSum(int[][] grid) {
            int n=grid.length;
            int m=grid[n-1].length;
          int dp[]=new int[m];
        dp[0]=grid[0][0];
     for(int i=1;i<dp.length;i++){
         dp[i]=grid[0][i]+dp[i-1];
     }
        for(int i=1;i<n;i++){
            dp[0]+=grid[i][0];
            for(int j=1;j<m;j++){
                dp[j]=Math.min(dp[j],dp[j-1])+grid[i][j];
            }
        }
        return dp[m-1];
    }

    /**
     *
     *  1,2
     *  2,1
     *  1,2,3
     *  1,3,2
     *  2,1,3
     *  2,3,1
     *  3,1,2
     *  3,2,1
     *  1,2,3,4
     *  1,3,2,4
     *  1,4,2,3
     *  2,1,3,4
     *  2,3,1,4
     *  2,4,1,3
     *
     *  初始要交换的 为 i=0 当找到一个 i 满足 nums[i]<nums[i+1] 则 t=i+1  break;
     * @param nums
     */
    public void nextPermutation(int[] nums) {
        if(nums.length<2)return;
      int i=nums.length-2;
        while(i>=0 && nums[i]>nums[i+1])i--;
        if(i>=0){
            int j=nums.length-1;
            while (nums[j]<nums[i])j--;
            exchange(nums,i,j);
        }
        reverseArray(nums,i+1,nums.length-1);
    }
    public void exchange(int[]nums,int i,int j){
         int temp=nums[i];
        nums[i]=nums[j];
        nums[j]=temp;
    }
    public void reverseArray(int []nums,int begin,int end){
        while(begin<end){
            exchange(nums,begin,end);
            begin++;
            end--;
        }

    }

    /**
     *  2  1+1
     *  3  2+1
     *  4  2+2
     *  5  3+2
     *  6  3+3
     *  7   2+dp[5]
     *  8  2+dp[6]
     *  9   dp[6]+3
     *  10 3+3+4    dp[7]+3
     *  11  3+3+3+2 dp[9]+2  dp[7]+3
     *  12  3+3+3+3 dp[9]+3  dp[10]+2
     *  I saw many solutions were referring to factors of 2 and 3. But why these two magic numbers? Why other factors do not work?
     Let's study the math behind it.

     For convenience, say n is sufficiently large and can be broken into any smaller real positive numbers. We now try to calculate which real number generates the largest product.
     Assume we break n into (n / x) x's, then the product will be xn/x, and we want to maximize it.

     Taking its derivative gives us n * xn/x-2 * (1 - ln(x)).
     The derivative is positive when 0 < x < e, and equal to 0 when x = e, then becomes negative when x > e,
     which indicates that the product increases as x increases, then reaches its maximum when x = e, then starts dropping.

     This reveals the fact that if n is sufficiently large and we are allowed to break n into real numbers,
     the best idea is to break it into nearly all e's.
     On the other hand, if n is sufficiently large and we can only break n into integers, we should choose integers that are closer to e.
     The only potential candidates are 2 and 3 since 2 < e < 3, but we will generally prefer 3 to 2. Why?

     Of course, one can prove it based on the formula above, but there is a more natural way shown as follows.

     6 = 2 + 2 + 2 = 3 + 3. But 2 * 2 * 2 < 3 * 3.
     Therefore, if there are three 2's in the decomposition, we can replace them by two 3's to gain a larger product.

     All the analysis above assumes n is significantly large. When n is small (say n <= 10), it may contain flaws.
     For instance, when n = 4, we have 2 * 2 > 3 * 1.
     To fix it, we keep breaking n into 3's until n gets smaller than 10, then solve the problem by brute-force.
     * @param n
     * @return
     */
    public int integerBreak(int n) {
          int[] dp=new int[n+1];
        dp[0]=1;
        dp[1]=1;
      //  dp[2]=2;
       if(n>3){
            for(int i=1;i<n+1;i++){
                for(int j=i;j>1;j--){
                    dp[i]=dp[i-j]*j>dp[i]?dp[i-j]*j:dp[i];
                }
          }
        }else{
         return n==3?2:1;
        }
        return dp[n];
    }


    /**
     * abab
     * ab
     * @param s
     * @param p
     * @return
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> list=new ArrayList<>();
        if (s == null || s.length() == 0 || p == null || p.length() == 0) return list;
        int plen=p.length();
        int slen=s.length();
        int count=p.length();
        int left=0,right=0;
        int []map=new int[256];
        for(int i=0;i<plen;i++){
            map[p.charAt(i)]++;
        }
        while(right<slen){
            if(map[s.charAt(right)] >=1){
                count--;
            }
            map[s.charAt(right)]--;
            right++;
            if(count==0){
                list.add(left);
            }
            if( right-left == plen ){
                if(map[s.charAt(left)] >= 0 ){
                    count++;
                }
                map[s.charAt(left)]++;
                left++;
                }

            }
        return list;
    }



    /**
     * Given encoded message "12", it could be decoded as "AB" (1 2) or "L" (12).
     *
     * @param s
     * @return
     */
    public int numDecodings(String s) {
        int n=s.length();
        if(n==0)  return 0;
        int  []dp=new int[n+1];
        dp[0]=1;
        dp[1]=s.charAt(0) != '0' ? 1 : 0;
        for(int i=2;i<dp.length;i++) {
            int first = Integer.valueOf(s.substring(i-1, i));
            int second = Integer.valueOf(s.substring(i-2, i));
            if(first >= 1 && first <= 9) {
                dp[i] += dp[i-1];
            }
            if(second >= 10 && second <= 26) {
                dp[i] += dp[i-2];
            }
        }
        return dp[n];

    }

    public int longestPalindrome(String s) {

        if(s==null || s.length()==0)
            return  0;
        Set<Character> set=new HashSet<>();
        int count=0;
        for(char c:s.toCharArray()){
            if(set.contains(c)){
                set.remove(c);
                count++;
            }else{
                set.add(c);
            }
        }
        if(!set.isEmpty())  return count*2+1;
            return count*2;

    }

    public char findTheDifference(String s, String t) {
        char[]array1=s.toCharArray();
        char[]array2=t.toCharArray();
        int a1=0;
        int a2=0;
        for(int i=0;i<array1.length;i++){
            a1+=(int)array1[i];
        }
        for(int i=0;i<array2.length;i++){
            a2+=(int)array2[i];
        }
     return (char) (a2-a1);
    }

        public int countSegments(String s) {
            if(s.equals("")){
                return 0;
            }
            if(s.length()==1){
                if(s.equals(" ")){
                    return 0;
                }else{
                    return 1;
                }
            }
            int first=0;
            int second=1;
            int count=0;
            int status1=0; //记录空格
            int slen=s.length();
            for(int i=1;i<slen;i++){
                if(s.charAt(i-1)!=' '&& s.charAt(i)==' '){
                    count++;
                }else{
                    if(s.charAt(i)==' '){
                        status1++;
                    }
                }
            }
            if( !(status1==slen-1) && s.charAt(slen-1)!=' ') {
                count++;
            }
        return count;
        }



    /**
     * 对于每一个 家只要找到前一个或者后一个heaters就可以了
     * 双指针,排序
     * @param houses
     * @param heaters
     * @return
     */
    public int findRadius(int[] houses, int[] heaters){
        if(heaters.length==0)
            return 0;
        Arrays.sort(houses);
        Arrays.sort(heaters);
        int i=0;
        int j=0;
        int maxLen=0;
     //   Arrays.binarySearch()
        while(i<houses.length){
               while(j+1<heaters.length && Math.abs(heaters[j+1]-houses[i])<=Math.abs(heaters[j]-houses[i])) j++;
            maxLen=Math.max(maxLen,Math.abs(heaters[j]-houses[i]));
            i++;
           }
        return  maxLen;
        }









    /**
     *    // memo[i][j] = the max number of strings that can be formed with i 0's and j 1's
     // from the first few strings up to the current string s
     // Catch: have to go from bottom right to top left
     // Why? If a cell in the memo is updated(because s is selected),
     // we should be adding 1 to memo[i][j] from the previous iteration (when we were not considering s)
     // If we go from top left to bottom right, we would be using results from this iteration => overcounting
     * @param strs
     * @param m
     * @param n
     * @return
     */
    public int findMaxForm(String[] strs, int m, int n) {
        int dp[][]=new int[m+1][n+1];

        for(String str:strs){
            int[] count=count(str);
          //  count[0] = str.replaceAll("1", "").length(); //just the zeros
          //  count[1] = str.replaceAll("0", "").length(); //just the ones

            for(int i=m;i>=count[0];i--){
                for(int j=n;j>=count[1];j--){
                    dp[i][j]=Math.max(dp[i][j],1+dp[i-count[0]][j-count[1]]);
                }
            }
        }
        return dp[m][n];

    }
    public int[]count(String str){
        int count[]=new int[2];
        int length=str.length();
        for(int i=0;i<length;i++){
            count[str.charAt(i)-'0']++;
        }
        return count;
    }


    int BitCount2( int n)
    {
         int c =0 ;
        for (c =0; c<n; ++c)
        {
            n &= (n -1) ; // 清除最低位的1
        }
        return c ;
    }
    public int hammingDistance(int x, int y) {

        int t=x^y;
    return    BitCount2(t);

    }



    public int myAtoi(String str) {
        if(str==null ||str.length()==0)
            return 0;
        str=str.trim();
        char firstChar=str.charAt(0);
        int sign=1,start=0,len=str.length();
        long sum=0;
        if(firstChar=='+'){
            sign=1;
            start++;
        }else if(firstChar=='-'){
            sign=-1;
            start++;
        }
        for(int i=start;i<len;i++){
            if(!Character.isDigit(str.charAt(i))){
                return (int)sum *sign;
            }
            sum=sum*10+str.charAt(i)-'0';
            if(sign==1 && sum>Integer.MAX_VALUE)
                return Integer.MAX_VALUE;
            if(sign==-1 && (-1)*sum<Integer.MIN_VALUE)
                return Integer.MIN_VALUE;
        }
        return (int)sum*sign;
    }


//    public boolean isPalindrome(int x) {
//        if(x^x);
//    }


    public int reverse(int x) {

        String t=Integer.toString(x);
        //符号位判断
        if(t.charAt(0)=='+'|| t.charAt(0)=='-'){
             char frist=t.charAt(0);

            char []array=t.substring(1).toCharArray();
            int begin=0;
            int end=array.length-1;
            while(begin<end){
                char temp=array[begin];
                array[begin]=array[end];
                array[end]=temp;
                begin++;
                end--;
            }
            String e=frist+"";
            String e2=new String(array);
            long temp=Long.parseLong(e+e2);
            if(temp>Integer.MAX_VALUE || temp<(-1)*Integer.MIN_VALUE)
                return 0;
            return  (int)temp;
        }else{
            //直接翻转
             char [] array=t.toCharArray();
            int begin=0;
            int end=array.length-1;
            while(begin<end){
                char temp=array[end];
                array[end]=array[begin];
                array[begin]=temp;
                begin++;
                end--;
            }
            long temp=Long.parseLong(new String(array));
            if(temp>Integer.MAX_VALUE)
                return 0;
          return   (int)temp;
        }


    }

    public boolean isPalindrome(int x) {
        int temp=new Solution().reverse(x);
        int t=temp^x;
        if(x<0)
            return false;
        return  t==0?true:false;
    }

    public int romanToInt(String s) {
        int[] nums=new int[s.length()];
        for(int i=0;i<s.length();i++){
            switch (s.charAt(i)){
                case 'M':
                    nums[i]=1000;
                    break;
                case 'D':
                    nums[i]=500;
                    break;
                case 'C':
                    nums[i]=100;
                    break;
                case 'L':
                    nums[i]=50;
                    break;
                case 'X' :
                    nums[i]=10;
                    break;
                case 'V':
                    nums[i]=5;
                    break;
                case 'I':
                    nums[i]=1;
                    break;
            }
        }
        int sum=0;
        for(int i=0;i<nums.length-1;i++){
            if(nums[i]<nums[i+1]){
                sum-=nums[i];
            }else{
                sum+=nums[i];
            }
        }
    return sum+nums[nums.length-1];

    }





    public int totalHammingDistance(int[] nums) {

        int count=0;
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length; j++){
                count+=hammingDistance(nums[i],nums[j]);
            }
        }




        return count;
    }

    public boolean isValid(String s) {
        Stack<Character> stack=new Stack<>();
        int len=s.length();
        for(int i=0;i<len;i++){
            char temp=s.charAt(i);
            if(temp=='(' || temp == '{' || temp == '[')
                stack.push(temp);
            else {
                switch (temp){
                    case ')':
                        if(!stack.empty() && stack.peek()=='(') {
                            stack.pop();
                            break;
                        }
                        return false;
                    case '}':
                        if(!stack.empty() && stack.peek()=='{'){
                            stack.pop() ;
                        break;
                    }
                        return false;
                    case ']':
                        if(!stack.empty() && stack.peek()=='[') {
                            stack.pop();
                            break;
                        }
                        return false;
                    default:
                        return false;
                }
            }
        }
     return stack.empty();
    }

    public int searchInsert(int[] nums, int target) {

        int left=0;
        int right=nums.length-1;
        while(left<=right){
            int middle=(left+right)/2;
            if(target==nums[middle])
                return middle;
            else if(target>nums[middle]){
                left=middle+1;
            }else{
                right=middle-1;
            }
        }
        return left;

    }
    public int[] searchRange(int[] nums, int target) {
        int result[]=new int[2];
        int left=0;
        int right=nums.length-1;
        while(left<=right){
            int middle=(left+right)/2;
            if(target==nums[middle]){
                result[0]=target;
                result[1]=target;
                int q=target-1;
                int w=target+1;
                if(q>0) {
                    while (nums[q] == target)
                        result[0] = q--;
                }
                if (w<nums.length){
                     while(nums[w]==target)
                         result[1]=w++;
                }
                return result;
            }

            else if(target>nums[middle]){
                left=middle+1;
            }else{
                right=middle-1;
            }
        }
        return new int[]{-1,-1};
    }


    /**
     * 11
     */
    public int maxArea(int[] height) {
        int left=0,right=height.length-1;
        int max=0;
        while(left<right){
            max=Math.max(max,Math.min(height[left],height[right]) * (right-left));
            if(height[left]<height[right]){
                    left++;
            }else{
                    right--;
            }
        }
    return max;

    }




    /**
     * 54
     */
//    public List<Integer> spiralOrder(int[][] matrix) {
//
//        List<Integer> result=new ArrayList<>();
//            int len1=matrix.length;
//            int len2=matrix[0].length;
//
//
//
//    }

    int sum=0;
    int[]queens;
    List<List<String>> result=new ArrayList<>();
    public List<List<String>> solveNQueens(int n) {
        queens=new int[n];
        dfs(0);
        return result;
    }
    /**
     *  52
     */
    public int totalNQueens(int n) {
        queens=new int[n];
        dfs(0);
        return sum;
    }
    public void dfs(int n){
        for(int i=0;i<queens.length;i++ ){
            if(n==queens.length){
                sum++;
                printQueues();
                return;
            }
            queens[n]=i;
            if(IsValid(n)){
             dfs(n+1);
            }
        }
    }
    boolean IsValid(int n){
        for(int i=0;i<n;i++){
            if(queens[i]==queens[n]){
                //同一列
                return false;
            }
            if(Math.abs(queens[n]-queens[i])==(n-i)) {//同一对角线
                return false;
                }
        }
        return true;
    }

    void printQueues(){
      List<String> list=new ArrayList<>();
        for(int i=0;i<queens.length;i++){
            StringBuilder builder=new StringBuilder("");
            for(int j=0;j<queens.length;j++){
                if(j==queens[i]){
                    builder.append("Q");
                }else{
                builder.append(".");
                }
            }
            list.add(builder.toString());
        }
        result.add(list);
    }


    /**
     * 56
     */
    static class Interval{
        int start;
        int end;
        Interval(){start=0;end=0;}
        Interval(int start,int end){this.start=start;this.end=end;}
    }

    public List<Interval> merge(List<Interval> intervals) {
        intervals.sort(new Comparator<Interval>() {
            @Override
            public int compare(Interval o1, Interval o2) {
            return  o1.start-o2.start;
            }
        });
       intervals=(ArrayList<Interval>)intervals;
            int len=intervals.size();

        for(int i=0;i<len-1;i++){
            int firststart=intervals.get(i).start;  //1
            int firstend=intervals.get(i).end;      //4
            int secondstart=intervals.get(i+1).start;   //2
            int secondend=intervals.get(i+1).end;       //3

            if(firstend>=secondstart){
                intervals.set(i,new Interval(firststart,Math.max(firstend,secondend)));
                intervals.remove(i+1);
                len--;
                i--;
                continue;
            }
            if(firstend>secondstart && secondend<firstend){
                //never reach here;
            }

        }

            return intervals;
    }


    /**
     *54
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> list=new ArrayList<>();
        if(matrix==null || matrix.length==0)return list;

        int top=0;
        int bottom=matrix.length-1;
        int left=0;
        int right=matrix[0].length-1;

        while(true){
            for(int j=left;j<=right;j++){
                list.add(matrix[top][j]);
            }
            top++;
            if(boundriesCross(left,right,bottom,top)){
                break;
            }
            for(int i=top;i<=bottom;i++){
                list.add(matrix[i][right]);
            }
            right--;
            if(boundriesCross(left,right,bottom,top)){
                break;
            }
            for(int j=right;j>=left;j--){
               list.add(matrix[bottom][j]);
            }
            bottom--;
            if(boundriesCross(left,right,bottom,top)){
                break;
            }
            for(int i=bottom;i>=top;i--){
                list.add(matrix[i][left]);
            }
            left++;
            if(boundriesCross(left,right,bottom,top)){
                break;
            }

        }
        return list;
    }
    private boolean boundriesCross(int left,int right,int bottom,int top){
        if(left>right || bottom<top){
            return true;
        }
        return false;
    }


    /**
     * 58
     */
    public int lengthOfLastWord(String s) {
        if(s ==null || s.equals(""))return 0;
        s=s.trim();
        int count=0;
        for(int i=s.length()-1;i>=0;i--){
            if(s.charAt(i)!=' '){
                count++;
            }else{
                break;
            }
        }
        return count;
    }


    /**
     *59
     */
    public int[][] generateMatrix(int n) {
        int left=0;
        int right=n-1;
        int top=0;
        int bottom=n-1;
        int count=1;
        int [][] result=new int[n][n]; // 行列
        //先上面 固定 top 走left
        while(true) {
            for (int i = left; i <= right; i++) {
                result[top][i] = count;
                count++;
            }
            top++;
            if(boundriesCross(left, right, bottom, top)){
             break;
            }
            //固定right走top
            for (int i = top; i <= bottom; i++) {
                result[i][right] = count;
                count++;
            }
            right--;
            if(boundriesCross(left, right, bottom, top)){
                break;
            }
            //固定 buttom 走 right
            for(int i=right;i>=left;i--){
                result[bottom][i]=count;
                count++;
            }
            bottom--;
            if(boundriesCross(left, right, bottom, top)){
                break;
            }
            //固定left 走bottom
            for(int i=bottom;i>=top;i--){
                result[i][left]=count;
                count++;
            }
            left++;
            if(boundriesCross(left, right, bottom, top)){
                break;
            }
        }

    return result;
    }



    public static void main(String[] args){
        int []a=new int[]{7,6,5,4,3,1,2,11,15,16,2,21};
        new Solution().fastSort(a,0,a.length-1);
        for(Integer s:a){

            System.out.println(s);
        }
    }


    public int findDuplicate(int[] nums) {
            int slow=0;
            int fast=0;
            int finder=0;

        while(true){
          slow=nums[slow];
            fast=nums[nums[fast]];
            if(slow==fast)
                break;
        }
        while (true){
            slow=nums[slow];
            finder=nums[finder];
            if(slow==finder)
                return slow;
        }

    }

    public int majorityElement(int[] nums) {
        HashMap<Integer,Integer> map=new HashMap<>(nums.length/2+1);

        for(int i=0;i<nums.length;i++){
          map.putIfAbsent(nums[i],0);
            map.put(nums[i],map.get(nums[i])+1);
            if(map.get(nums[i])>nums.length/2)
                return nums[i];
        }
        return -1;
    }


}











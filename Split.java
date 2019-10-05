import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Split {

	static int[][] matrix = null;
	static Map<Integer, Integer> getAmounts = new HashMap<>();
	static Map<Integer, Integer> giveAmounts = new HashMap<>();
	static Map<Integer, Integer> finalgiveAmounts = new ConcurrentHashMap<>();
	static String[] arr;

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("Number of transactions: ");
		int n = s.nextInt();
		System.out.print("Number of people: ");
		int tot = s.nextInt();
		System.out.println("Enter the names: ");
		arr = new String[tot];
		for (int i = 0; i < tot; i++)
			arr[i] = s.next();
		matrix = new int[n][n];
		Set<String> payees = new HashSet<>();
		Set<String> payers = new HashSet<>();
		for (int i = 0; i < n; i++) {
			int payersNumber = s.nextInt(); // Number of payers in the transaction
			payers.clear();
			payees.clear();
			for (int k = 0; k < payersNumber; k++) {
				payers.add(s.next()); // Stores the payers List for the transaction
			}
			int payeesNumber = s.nextInt(); // Number of payees in the transaction
			for (int j = 0; j < payeesNumber; j++) {
				payees.add(s.next()); // Stores the payees List for the transaction
			}
			int amount = s.nextInt(); // Amount paid in the transaction
			createMatrix(payers, payees, amount, tot);
		}

		calculateAmounts(matrix, n);
		finalTransactions(getAmounts, giveAmounts);
	}

	private static void createMatrix(Set<String> payers, Set<String> payees, int amount, int tot) {
		Set<Integer> payers_List = new HashSet<Integer>();
		Set<Integer> payees_List = new HashSet<Integer>();
		if (payers.isEmpty() || payees.isEmpty()) {
			System.out.print("Invalid Input");
		} else {
			for (String i : payers) {
				for (int j = 0; j < tot; j++) {
					if (i.equals(arr[j]))
						payers_List.add(j);
				}
			}
			for (String i : payees) {
				for (int j = 0; j < tot; j++) {
					if (i.equals(arr[j]))
						payees_List.add(j);
				}
			}
			for (Integer i : payers_List) {
				for (Integer j : payees_List) {
					int amountToBePaid = (amount / payees.size()) / payers.size(); // Amount Calculation for each Payee
																					// in a transaction
					if (i != j) {
						if (matrix[i][j] == 0)
							matrix[i][j] = amountToBePaid;
						else
							matrix[i][j] += amountToBePaid;
					}
				}
			}
		}

	}

	private static void calculateAmounts(int[][] matrix, int n) {
		for (int i = 0; i < n; i++) {
			Integer sum = 0;
			for (int j = 0; j < n; j++) {
				sum = sum + matrix[i][j];
				getAmounts.put(i, sum);
			}
		}
		for (int i = 0; i < n; i++) {
			Integer sum = 0;
			for (int j = 0; j < n; j++) {
				sum = sum + matrix[j][i];
				giveAmounts.put(i, sum);
			}
		}
	}

	private static void finalTransactions(Map<Integer, Integer> getAmounts, Map<Integer, Integer> giveAmounts) {
		int amt = 0;
		if (getAmounts.isEmpty() || giveAmounts.isEmpty()) {
			System.out.print("Invalid Input");
		} else {
			for (Map.Entry<Integer, Integer> entry : getAmounts.entrySet()) {
				Integer key = entry.getKey();
				Integer value = entry.getValue();
				for (Map.Entry<Integer, Integer> entry1 : giveAmounts.entrySet()) {
					Integer key1 = entry1.getKey();
					Integer value1 = entry1.getValue();
					if (key == key1) {
						amt = value - value1;
					}
				}
				finalgiveAmounts.put(key, amt);
			}
		}
		printTransactions(finalgiveAmounts);

	}

	private static void printTransactions(Map<Integer, Integer> finalgiveAmounts) {
		if (getAmounts.isEmpty() || giveAmounts.isEmpty()) {
			System.out.print("Invalid Input");
		} else {
			for (Map.Entry<Integer, Integer> entry : finalgiveAmounts.entrySet()) {
				int amt = finalgiveAmounts.get(finalgiveAmounts.keySet().iterator().next());
				Integer key = entry.getKey();
				Integer value = entry.getValue();
				if (value == amt)
					continue;
				if ((value < 0 && amt > 0) || (value > 0 && amt < 0)) {
					if (value + amt < 0) {
						if (amt > 0) {
							System.out.println(arr[key] + " pays->" + amt + " to->"
									+ arr[finalgiveAmounts.keySet().iterator().next()]);
							Integer toberemoved = finalgiveAmounts.keySet().iterator().next();
							finalgiveAmounts.remove(toberemoved);
							finalgiveAmounts.put(key, amt + value);
						}
					} else if (value + amt > 0) {
						if (amt > 0) {
							System.out.println(arr[key] + " pays->" + (-value) + " to->"
									+ arr[finalgiveAmounts.keySet().iterator().next()]);
							finalgiveAmounts.remove(key);
							finalgiveAmounts.put(finalgiveAmounts.keySet().iterator().next(), amt + value);
						}
					} else if (value + amt == 0) {
						if (amt > 0)
							System.out.println(arr[key] + " pays->" + (-value) + " to->"
									+ arr[finalgiveAmounts.keySet().iterator().next()]);
						else
							System.out.println(arr[finalgiveAmounts.keySet().iterator().next()] + " pays->" + (value)
									+ " to->" + arr[key]);
						finalgiveAmounts.remove(key);
						Integer toberemoved = finalgiveAmounts.keySet().iterator().next();
						finalgiveAmounts.remove(toberemoved);

					}
					break;
				}
			}
		}
		if (!finalgiveAmounts.isEmpty())
			printTransactions(finalgiveAmounts);
	}
}

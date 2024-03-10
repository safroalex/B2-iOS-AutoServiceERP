//
//  TotalCostViewModel.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//

import Foundation

class TotalCostViewModel: ObservableObject {
    @Published var totalCost: Double = 0.0

    func loadTotalCost(fromDate: String, toDate: String) {
        TotalCostNetworkManager.shared.fetchTotalCost(fromDate: fromDate, toDate: toDate) { result in
            switch result {
            case .success(let totalCost):
                DispatchQueue.main.async {
                    self.totalCost = totalCost
                }
            case .failure(let error):
                print("Error fetching total cost: \(error.localizedDescription)")
                DispatchQueue.main.async {
                    self.totalCost = 0.0
                }
            }
        }
    }
}


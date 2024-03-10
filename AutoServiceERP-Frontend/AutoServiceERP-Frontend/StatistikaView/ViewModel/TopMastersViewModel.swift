//
//  TopMastersViewModel.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//

import Foundation

class TopMastersViewModel: ObservableObject {
    @Published var topMasters: [MasterStatistic] = []

    func loadTopMasters(fromDate: String, toDate: String) {
        TopMastersNetworkManager.shared.fetchTopMasters(fromDate: fromDate, toDate: toDate) { result in
            switch result {
            case .success(let masters):
                DispatchQueue.main.async {
                    self.topMasters = masters
                }
            case .failure(let error):
                print("Error fetching top masters: \(error.localizedDescription)")
            }
        }
    }
}

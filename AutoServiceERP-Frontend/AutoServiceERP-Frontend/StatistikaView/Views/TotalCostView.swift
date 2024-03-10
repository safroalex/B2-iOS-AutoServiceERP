//
//  TotalCostView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//

import SwiftUI

struct TotalCostView: View {
    @StateObject var viewModel = TotalCostViewModel()

    var body: some View {
        VStack {
            Text("Общая стоимость за период:")
            Text("\(viewModel.totalCost, specifier: "%.2f") руб.")
                .font(.title)
        }
        .onAppear {
            viewModel.loadTotalCost(fromDate: "2022-01-01T00:00:00+00:00", toDate: "2024-01-31T23:59:59+00:00")
        }
    }
}



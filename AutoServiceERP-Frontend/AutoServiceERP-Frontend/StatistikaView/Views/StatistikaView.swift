//
//  StatistikaView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//

import SwiftUI

struct StatistikaView: View {
    @State private var selectedStatistic = "Общая стоимость"
    let statisticsOptions = ["Общая стоимость", "Топ мастера"]
    
    var body: some View {
        NavigationView {
            VStack {
                Picker("Статистика", selection: $selectedStatistic) {
                    ForEach(statisticsOptions, id: \.self) {
                        Text($0)
                    }
                }
                .pickerStyle(SegmentedPickerStyle())
                
                if selectedStatistic == "Общая стоимость" {
                    TotalCostView()
                } else if selectedStatistic == "Топ мастера" {
                    TopMastersView()
                }
            }
            .navigationTitle("Статистика")
        }
    }
}




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
                Picker("Выберите вид статистики:", selection: $selectedStatistic) {
                    ForEach(statisticsOptions, id: \.self) {
                        Text($0)
                    }
                }
                .pickerStyle(SegmentedPickerStyle())
                .padding()
                
                // Использование Spacer для создания вертикального пространства
                Spacer()
                
                if selectedStatistic == "Общая стоимость" {
                    TotalCostView()
                } else if selectedStatistic == "Топ мастера" {
                    TopMastersView()
                }
                
                Spacer() // Использование Spacer внизу для симметрии
            }
            .padding() // Добавление отступа к VStack для всей секции
            .navigationTitle("Статистика")
            .navigationBarTitleDisplayMode(.inline) // Режим отображения заголовка навигации
        }
    }
}





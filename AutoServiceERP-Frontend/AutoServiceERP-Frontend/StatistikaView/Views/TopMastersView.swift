//
//  TopMastersView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//

import SwiftUI

struct TopMastersView: View {
    @StateObject private var viewModel = TopMastersViewModel()
    @State private var fromDate = Date().addingTimeInterval(-86400 * 30) // Начальная дата по умолчанию: 30 дней назад
    @State private var toDate = Date() // Конечная дата по умолчанию: текущая дата

    var body: some View {
        NavigationView {
            VStack {
                Form {
                    Section(header: Text("Выберите период")) {
                        DatePicker("С:", selection: $fromDate, displayedComponents: .date)
                        DatePicker("По:", selection: $toDate, displayedComponents: .date)
                    }
                    
                    Button("Показать", action: loadData)
                }
                ScrollView {
                    LazyVStack {
                        ForEach(viewModel.topMasters) { master in
                            HStack {
                                VStack(alignment: .leading) {
                                    Text(master.name)
                                        .font(.headline)
                                    Text("Количество работ: \(master.numberOfWorks)")
                                        .font(.subheadline)
                                }
                                Spacer()
                            }
                            .padding(.vertical)
                        }
                    }
                }
            }
            .navigationTitle("Топ мастера")
        }
    }
    
    private func loadData() {
        let dateFormatter = ISO8601DateFormatter()
        dateFormatter.timeZone = TimeZone(secondsFromGMT: 0) // Установка часового пояса UTC
        let formattedFromDate = dateFormatter.string(from: fromDate)
        let formattedToDate = dateFormatter.string(from: toDate)
        viewModel.loadTopMasters(fromDate: formattedFromDate, toDate: formattedToDate)
    }
}


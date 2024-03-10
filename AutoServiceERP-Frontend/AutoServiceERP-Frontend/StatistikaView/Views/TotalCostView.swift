//
//  TotalCostView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//

import SwiftUI

struct TotalCostView: View {
    @StateObject var viewModel = TotalCostViewModel()
    @State private var showingDatePicker = false
    @State private var fromDate = Date()
    @State private var toDate = Date()
    
    var body: some View {
        NavigationView {
            VStack(alignment: .leading) {
                Button("Выберите период") {
                    self.showingDatePicker = true
                }
                .padding()
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(8)

                if viewModel.totalCost > 0 {
                    Text("Общая стоимость за период:")
                    Text("\(viewModel.totalCost, specifier: "%.2f") руб.")
                        .font(.title)
                }
            }
            .padding()
            .sheet(isPresented: $showingDatePicker) {
                DatePickerView(fromDate: $fromDate, toDate: $toDate) {
                    let fromDateStr = ISO8601DateFormatter().string(from: fromDate)
                    let toDateStr = ISO8601DateFormatter().string(from: toDate)
                    viewModel.loadTotalCost(fromDate: fromDateStr, toDate: toDateStr)
                }
            }
            .navigationTitle("Статистика")
        }
    }
}

struct DatePickerView: View {
    @Binding var fromDate: Date
    @Binding var toDate: Date
    var completion: () -> Void
    
    var body: some View {
        VStack {
            DatePicker("С:", selection: $fromDate, displayedComponents: [.date])
            DatePicker("По:", selection: $toDate, displayedComponents: [.date])
            Button("Применить") {
                completion()
            }
            .padding()
            .background(Color.blue)
            .foregroundColor(.white)
            .cornerRadius(8)
        }
        .padding()
    }
}

